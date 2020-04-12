package com.belluk.movapps.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.belluk.movapps.model.Seat
import com.belluk.movapps.model.Tiket
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class MyService : Service() {
    var counter = 0
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            startMyOwnForeground()
        }else{
            startForeground(1, Notification())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        lateinit var preferencesUsers: PreferencesUsers
        var dataList = ArrayList<Tiket>()
        var dataSeat = ArrayList<Seat>()
        lateinit var mDatabase:DatabaseReference
        var dateFormat: SimpleDateFormat? = null
        lateinit var mdb: DatabaseReference
        lateinit var mHistory:DatabaseReference
        val NOTIFICATION_CHANNEL_ID = "mov.apps"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
        dateFormat = SimpleDateFormat("d/M/yyyy")
        mdb = FirebaseDatabase.getInstance().reference
        preferencesUsers = PreferencesUsers(this)
        mDatabase = FirebaseDatabase.getInstance().getReference("Tiket").child(preferencesUsers.getValues("user").toString())
        mHistory = FirebaseDatabase.getInstance().getReference("History")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyService,""+error.message, Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(dataSnap: DataSnapshot) {
                dataList.clear()
                for (i in dataSnap.children){
                    val data = i.getValue(Tiket::class.java)
                    dataList.add(data!!)
                }
                    for (i in dataList.indices){
                        val dateTiket = dataList[i].date
                        val user = dataList[i].user
                        val kode = dataList[i].kodeTiket
                        val nama = dataList[i].nama
                        val bioskop = dataList[i].bioskop
                        val cinema = dataList[i].cinema
                        val genre = dataList[i].genre
                        val poster = dataList[i].poster
                        val rating = dataList[i].rating
                        val timer = dataList[i].timer
                        val date = dateFormat!!.format(Date())
                        val date1 = dateFormat!!.parse(date)
                        val date2 = dateFormat!!.parse(dateTiket)
                        if (date1.after(date2)){
                            val dataTiket = Tiket(nama,bioskop,genre,user,dateTiket,poster,rating,timer,kode,cinema)
                            val seat = mdb.child("Tiket").child(user.toString()).child(kode.toString()).child("kursi")
                            val que = mdb.child("Tiket").child(user.toString()).orderByChild("kodeTiket").equalTo(kode.toString())
                            que.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e(ContentValues.TAG, "onCancelled", p0.toException());
                                }
                                override fun onDataChange(p0: DataSnapshot) {
                                    for (appleSnapshot in p0.children) {
                                        appleSnapshot.ref.removeValue()
                                    }
                                }
                            })
                            seat.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Log.e(ContentValues.TAG, "onCancelled", p0.toException());
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for (i in p0.children){
                                        val seat = i.getValue(Seat::class.java)
                                        dataSeat.add(seat!!)
                                    }
                                    mHistory.child(user.toString()).child(kode.toString()).setValue(dataTiket)
                                    for (a in dataSeat.indices){
                                        mdb.child("Seat").child(dataList[i].bioskop.toString()).child(dataList[i].cinema.toString()).child("seat").child(dataSeat[a].golSeat.toString()).child(dataSeat[a].nama.toString()).child("status").setValue("0")
                                    }
                                }

                            })
                        }


                    }
            }

        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("Count", "=========  " + counter++)
            }
        }
        timer!!.schedule(timerTask, 1000, 1000) //
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
