package com.belluk.movapps.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.belluk.movapps.R
import com.belluk.movapps.model.User
import com.belluk.movapps.utils.PreferencesUsers
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    private lateinit var pDialog: ProgressDialog
    lateinit var iUsername:String
    lateinit var iPassword:String
    lateinit var mDatabase:DatabaseReference
    lateinit var preferences:PreferencesUsers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = PreferencesUsers(this)
        preferences.setValues("onboarding","1")
        if(preferences.getValues("status").equals("1")){
            finishAffinity()
            val intent = Intent(this@SignInActivity,HomeScreenActivity::class.java)
            startActivity(intent)
        }
        txt_username.requestFocus()
        btn_signin.setOnClickListener {
            iUsername = txt_username.text.toString()
            iPassword = txt_password.text.toString()
            if (iUsername.equals("")) {
                txt_username.error = "Silahkan tulis Username Anda"
                txt_username.requestFocus()
            } else if (iPassword.equals("")) {
                txt_password.error = "Silahkan tulis Password Anda"
                txt_password.requestFocus()
            } else {
                val statusUsername = iUsername.indexOf(".")
                if (statusUsername >=0) {
                    txt_username.error = "Silahkan tulis Username Anda tanpa ."
                    txt_username.requestFocus()
                }else{
                    pushLogin(iUsername, iPassword)
                }
            }
        }
        btn_register.setOnClickListener {
            val register = Intent(this,RegisterActivity::class.java)
            startActivity(register)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)
        pDialog.setMessage("Memuat..")
        pDialog.show()
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if(user == null){
                    Toast.makeText(this@SignInActivity, "User tidak ditemukan",Toast.LENGTH_LONG).show()
                    hideDialog()
                }else{
                    if(user.password.equals(iPassword)){
                        hideDialog()
                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()
                        preferences.setValues("nama",user.nama.toString())
                        preferences.setValues("user",user.username.toString())
                        preferences.setValues("url",user.url.toString())
                        preferences.setValues("image",user.image.toString())
                        preferences.setValues("password",user.password.toString())
                        preferences.setValues("email",user.email.toString())
                        preferences.setValues("saldo",user.saldo!!.toString())
                        preferences.setValues("status","1")
                        finishAffinity()
                        val intent = Intent(this@SignInActivity,HomeScreenActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@SignInActivity, "Password / Username Salah", Toast.LENGTH_LONG).show()
                        hideDialog()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
                hideDialog()
            }
        })
    }
    //hidden dialog
  private fun hideDialog() {

        if (pDialog.isShowing)
              pDialog.dismiss()
    }
}
