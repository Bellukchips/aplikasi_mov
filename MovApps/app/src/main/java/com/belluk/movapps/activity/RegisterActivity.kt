package com.belluk.movapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.belluk.movapps.R
import com.belluk.movapps.model.User
import com.belluk.movapps.utils.PreferencesUsers
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    lateinit var iemail:String
    lateinit var inama:String
    lateinit var ipassword:String
    lateinit var iusername:String
    private lateinit var mFirebaseDatabase:DatabaseReference
    private lateinit var mFirebaseInstance:FirebaseDatabase
    private lateinit var mDatabase :DatabaseReference
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseDatabase= mFirebaseInstance.getReference("User")
        preferencesUsers = PreferencesUsers(this)
        btn_next_register.setOnClickListener {
            iemail = txt_email_register.text.toString()
            inama = txt_nama_register.text.toString()
            ipassword = txt_password_register.text.toString()
            iusername = txt_username_register.text.toString()

            if (iusername.equals("")){
                txt_username_register.error = "Silahkan Isi Username"
                txt_username_register.requestFocus()
            }else if(ipassword.equals("")){
                txt_password_register.error = "Silahkan Isi Password"
                txt_password_register.requestFocus()
            }else if(inama.equals("")){
                txt_nama_register.error = "Silahkan Isi Nama"
                txt_nama_register.requestFocus()
            }else if(iemail.equals("")){
                txt_email_register.error = "Silahkan Isi Email"
                txt_email_register.requestFocus()
            }else{
                var statusUserName = iusername.indexOf(".")
                if(statusUserName>=0){
                    txt_username_register.error = "Silahkan Tulis Username Tanpa ."
                }else{
                    saveNewUser(iusername,ipassword,inama,iemail)
                }
            }
        }
    }

    private fun saveNewUser(iusername: String, ipassword: String, inama: String, iemail: String) {
        val user = User()
        user.email = iemail
        user.nama = inama
        user.password = ipassword
        user.username = iusername
        user.saldo = "0"

        if (iusername != null) {
            checkingUser(iusername, user)
        }
    }
        private fun checkingUser(iusername: String, data: User) {
        mFirebaseDatabase.child(iusername).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnap: DataSnapshot) {
                val user = dataSnap.getValue(User::class.java)
                if(user == null){
                    mFirebaseDatabase.child(iusername).setValue(data)
                    preferencesUsers.setValues("nama",data.nama.toString())
                    preferencesUsers.setValues("user",data.username.toString())
                    preferencesUsers.setValues("saldo","0")
                    preferencesUsers.setValues("url","")
                    preferencesUsers.setValues("password",data.password.toString())
                    preferencesUsers.setValues("image","")
                    preferencesUsers.setValues("email",data.email.toString())
                    preferencesUsers.setValues("status","1")
                    finish()
                    val intent = Intent(this@RegisterActivity,RegisterPhotosScreenActivity::class.java).putExtra("nama",data.nama).putExtra("saldo",data.saldo)
                    startActivity(intent)
                }else{
                    Snackbar.make(btn_next_register,"Username sudah di gunakan",Snackbar.LENGTH_LONG).show()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity,""+error.message, Toast.LENGTH_LONG).show()

            }
        })
    }

}
