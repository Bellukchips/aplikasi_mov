package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.belluk.movapps.R
import kotlinx.android.synthetic.main.activity_on_boarding_three.*

class OnBoardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_three)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        btn_next3.setOnClickListener {
            finishAffinity()
            val next = Intent(this,SignInActivity::class.java)
            startActivity(next)
        }
    }
}
