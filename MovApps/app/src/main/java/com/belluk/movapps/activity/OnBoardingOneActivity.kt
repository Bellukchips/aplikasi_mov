package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.belluk.movapps.R
import com.belluk.movapps.utils.PreferencesUsers
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {
    lateinit var preferencesUsers: PreferencesUsers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        if(preferencesUsers.getValues("onboarding").equals("1")){
            finishAffinity()
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
        btn_next.setOnClickListener {
            val next  = Intent(this,OnBoardingTwoActivity::class.java)
            startActivity(next)
        }
        btn_lewati.setOnClickListener {
            val skip  = Intent(this,SignInActivity::class.java)
            startActivity(skip)
            finish()
        }
    }
}
