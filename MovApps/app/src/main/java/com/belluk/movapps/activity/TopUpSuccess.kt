package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.belluk.movapps.R
import kotlinx.android.synthetic.main.activity_top_up_success.*

class TopUpSuccess : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up_success)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        button_home.setOnClickListener {
            finish()
            startActivity(Intent(this,HomeScreenActivity::class.java))
        }
    }
}
