package com.belluk.movapps.activity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.belluk.movapps.R
import com.belluk.movapps.adapter.CheckoutAdapter
import com.belluk.movapps.model.*
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckOutActivity : AppCompatActivity() {
    private lateinit var pDialog:ProgressDialog
    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var preferencesUsers: PreferencesUsers
    private var date: String? = null
    private var dateTiket: String? = null
    lateinit var mDatabase:DatabaseReference
    lateinit var mDbUser:DatabaseReference
    lateinit var mDbWallet:DatabaseReference
    lateinit var mSeat:DatabaseReference
    private var dateFormat: SimpleDateFormat? = null
    private var calendar: Calendar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        mDatabase = FirebaseDatabase.getInstance().getReference("Tiket")
        mSeat = FirebaseDatabase.getInstance().getReference("Seat")
        mDbUser = FirebaseDatabase.getInstance().getReference("User")
        mDbWallet = FirebaseDatabase.getInstance().getReference("Wallet")
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>
        val dateGet = intent.getStringExtra("date")
//        val dataSeat = intent.getParcelableExtra<Checkout>("kursi")
        var data =intent.getParcelableExtra<Film>("datas")
        val dataBioskop = intent.getParcelableExtra<Bioskop>("bioskop")
        for (a in dataList.indices){
            total +=dataList[a].harga!!.toInt()
        }
        date_text.text = dateGet
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        id_total.text = formatRupiah.format(total.toDouble())
        recyle_seat.layoutManager =LinearLayoutManager(this)
        recyle_seat.adapter = CheckoutAdapter(dataList){
        }
        nama_cinema.text = dataBioskop.nama+" , "+data.cinema
        tv_saldo.text = formatRupiah.format(preferencesUsers.getValues("saldo")!!.toDouble())
        dateFormat =  SimpleDateFormat("MM/dd/yyyy")
        calendar = Calendar.getInstance()
        val saldo = preferencesUsers.getValues("saldo")!!.toDouble()
        val totalharga = total.toDouble()
        val hasilHitung = saldo - totalharga
        val namaFilm = data.title
        val bioskop = dataBioskop.nama
        val cinema = data.cinema
        val user = preferencesUsers.getValues("user")
        val genreFilm = data.genre
        val date = dateGet
        val timer = data.jam
        val poster = data.poster
        val rating = data.rating
        val dateBuy = dateFormat!!.format(Date())
        if (saldo < totalharga){
            btn_bayar.visibility = View.INVISIBLE
            txt_warning_checkOut.visibility = View.VISIBLE
        }else{
            btn_bayar.visibility = View.VISIBLE
            txt_warning_checkOut.visibility = View.INVISIBLE
        }
        btn_bayar.setOnClickListener {
            pDialog = ProgressDialog(this)
            pDialog.setCancelable(false)
            pDialog.setTitle("Prossesing")
            pDialog.show()
            //save data
            val random = GenerateString.randomString(7)
            val dataTiket = Tiket(namaFilm,bioskop,genreFilm,user,date,poster,rating,timer,random,cinema,dateBuy)
            val saveWalet = Wallet(user,random,namaFilm,dateBuy,totalharga.toString(),"0")
            mDbWallet.child(user.toString()).child(random).setValue(saveWalet)
            mDatabase.child(user.toString()).child(random).setValue(dataTiket)
            for (a in dataList.indices){
                mDatabase.child(user.toString()).child(random).child("kursi").child(dataList[a].kursi.toString()).child("nama").setValue(dataList[a].kursi)
                mDatabase.child(user.toString()).child(random).child("kursi").child(dataList[a].kursi.toString()).child("golSeat").setValue(dataList[a].golSeat)
            }
            ///
            val intent = Intent(this,CheckOutSuccessActivity::class.java).putExtra("tiket",dataList).putExtra("film",data).putExtra("bioskop",dataBioskop).putExtra("mytiket",dataTiket).putExtra("date",dateGet)
            startActivity(intent)
            mDbUser.child(preferencesUsers.getValues("user").toString()).child("saldo").setValue(hasilHitung.toString())
            preferencesUsers.setValues("saldo",hasilHitung.toString())
            for (i in dataList.indices){
                mSeat.child(dataList[i].bioskop.toString()).child(dataList[i].cinema.toString()).child("seat").child(dataList[i].golSeat.toString()).child(dataList[i].kursi.toString()).child("status").setValue("1")
            }
            hideDialog()
            showNotif(data)
            finishAffinity()
        }
        btn_cancel_checkout.setOnClickListener {
            val intent = Intent(this,HomeScreenActivity::class.java)
            startActivity(intent)
            for (i in dataList.indices){
                mSeat.child(dataList[i].bioskop.toString()).child(dataList[i].cinema.toString()).child("seat").child(dataList[i].golSeat.toString()).child(dataList[i].kursi.toString()).child("status").setValue("0")
            }
            finishAffinity()
        }

    }
    private var broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val notConnect = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnect){
               btn_bayar.isClickable = false
            }else{
                btn_bayar.isClickable = true
            }
        }
    }
    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
    object GenerateString {
        /**
         * Genera una password RANDOM
         */
        const val DATA =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        var RANDOM = Random()
        fun randomString(len: Int): String {
            val sb = StringBuilder(len)
            for (i in 0 until len) {
                sb.append(DATA[RANDOM.nextInt(DATA.length)])
            }
            return sb.toString()
        }
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Tergesah Membeli Tiket ? Jika Ya Maka Tiket Akan Di Batalkan Otomatis")
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { dialog, id -> this.finish()
                for (i in dataList.indices){
                    mSeat.child(dataList[i].bioskop.toString()).child(dataList[i].cinema.toString()).child("seat").child(dataList[i].golSeat.toString()).child(dataList[i].kursi.toString()).child("status").setValue("0")
                }
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }


    private fun showNotif(datas: Film) {
       val dateGet = intent.getStringExtra("date")
        val dataBioskop = intent.getParcelableExtra<Bioskop>("bioskop")
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>
        var data =intent.getParcelableExtra<Film>("datas")
        val namaFilm = data.title
        val bioskop = dataBioskop.nama
        val cinema = data.cinema
        val user = preferencesUsers.getValues("user")
        val genreFilm = data.genre
        val dateBuy = date
        val timer = data.jam
        val poster = data.poster
        val rating = data.rating
        val random = GenerateString.randomString(5)
        val dataTiket = Tiket(namaFilm,bioskop,genreFilm,user,dateBuy,poster,rating,timer,random,cinema)
        val NOTIFICATION_CHANNEL_ID = "channel_mov_apps_notif"
        val context = this.applicationContext
        var notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "Mov Apps Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        val mIntent = Intent(this, TiketActivity::class.java).putExtra("tiketss",datas).putExtra("tiket",dataList).putExtra("bioskop",dataBioskop).putExtra("mytiket",dataTiket).putExtra("date",dateGet)
        val bundle = Bundle()
        bundle.putParcelable("tiketss", datas)
        mIntent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.logo
                )
            )
            .setTicker("notif bwa starting")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Sukses Terbeli")
            .setContentText("Tiket "+datas.title+" berhasil kamu dapatkan. Enjoy the movie!")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, builder.build())
    }
    private fun hideDialog() {

        if (pDialog.isShowing)
            pDialog.dismiss()
    }
}
