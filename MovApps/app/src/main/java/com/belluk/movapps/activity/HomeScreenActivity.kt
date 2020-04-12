package com.belluk.movapps.activity

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.belluk.movapps.R
import com.belluk.movapps.db.MovieHelper
import com.belluk.movapps.fragment.DashboardFragment
import com.belluk.movapps.fragment.NoConnectionFragment
import com.belluk.movapps.fragment.ProfileFragment
import com.belluk.movapps.fragment.TiketFragment
import com.belluk.movapps.service.MyService
import kotlinx.android.synthetic.main.activity_home_screen.*


class HomeScreenActivity : AppCompatActivity() {
 var number:Int = 0
    var mServiceIntent: Intent? = null
    private var mYourService: MyService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val dashboardFragment = DashboardFragment()
        setFragment(dashboardFragment)
        changeIcon(home_button,R.drawable.ic_logo_home_on_change)
        home_button.isClickable = false
        home_button.setOnClickListener{
            number = 1
            home_button.isClickable = false
            tiket_button.isClickable = true
            user_button.isClickable = true
            changeIcon(home_button,R.drawable.ic_logo_home_on_change)
            changeIcon(tiket_button,R.drawable.ic_logo_tiket)
            changeIcon(user_button,R.drawable.ic_logo_useer)
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        tiket_button.setOnClickListener {
            number = 2
            tiket_button.isClickable = false
            home_button.isClickable = true
            user_button.isClickable = true
            changeIcon(home_button,R.drawable.ic_logo_home)
            changeIcon(tiket_button,R.drawable.ic_logo_tiket_on_change)
            changeIcon(user_button,R.drawable.ic_logo_useer)
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        user_button.setOnClickListener{
            number = 3
            user_button.isClickable = false
            tiket_button.isClickable = true
            home_button.isClickable = true
            changeIcon(home_button,R.drawable.ic_logo_home)
            changeIcon(tiket_button,R.drawable.ic_logo_tiket)
            changeIcon(user_button,R.drawable.ic_logo_useer_on_change)
            registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }
    private var broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val connectF = NoConnectionFragment()
            val dashboardFragment = DashboardFragment()
            val tiketFragment = TiketFragment()
            val profileFragment = ProfileFragment()
            val notConnect = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnect){
                setFragment(connectF)
            }else{
             when(number){
                 1 -> setFragment(dashboardFragment)
                 2 -> setFragment(tiketFragment)
                 3 -> setFragment(profileFragment)
             }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        mYourService = MyService()
        mServiceIntent = Intent(this, mYourService!!.javaClass)
        if (!isMyServiceRunning(mYourService!!.javaClass)) {
            startService(mServiceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.close()
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
    private fun setFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame,fragment)
        fragmentTransaction.commit()
    }
    private fun changeIcon(imgIcon:ImageView,int:Int){
        imgIcon.setImageResource(int)
    }
}
