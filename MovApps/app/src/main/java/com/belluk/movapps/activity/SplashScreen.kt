package com.belluk.movapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belluk.movapps.R
import android.content.Context
import android.os.Handler
import android.content.Intent
import android.net.ConnectivityManager
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {
    private lateinit var conMgr: ConnectivityManager
    override fun onStart() {
        connectionService()
        super.onStart()
    }
    private val splashTime= 4000L
    //private var gifView:GifView?=null
    private lateinit var myhandler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        myhandler = Handler()
        conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }
    private fun connectionService(){

        run {
            if (conMgr.activeNetworkInfo != null
                && conMgr.activeNetworkInfo!!.isAvailable
                && conMgr.activeNetworkInfo!!.isConnected
            ) {
                myhandler.postDelayed({
                    goToActivity()
                },splashTime)
            } else {
                val i = Intent(applicationContext,NoConnection::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    private fun goToActivity(){
        val boardingOne = Intent(applicationContext, OnBoardingOneActivity::class.java)
        startActivity(boardingOne)
        finish()
    }
}
