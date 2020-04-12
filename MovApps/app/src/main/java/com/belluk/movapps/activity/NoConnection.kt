package com.belluk.movapps.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.belluk.movapps.R


class NoConnection : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)


    }
    private var broadcastReceiver :BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(ctx: Context?, intent: Intent?) {
                val notConnect = intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnect){
                return
            }else{
                val i = Intent(this@NoConnection,SplashScreen::class.java)
                startActivity(i)
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
}

