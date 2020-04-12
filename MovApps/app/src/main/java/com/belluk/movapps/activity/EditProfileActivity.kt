package com.belluk.movapps.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.belluk.movapps.R
import com.belluk.movapps.model.User
import com.belluk.movapps.utils.PreferencesUsers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_photoscreen.*
import kotlinx.android.synthetic.main.item_edit_profile.*
import java.util.*
import kotlin.collections.ArrayList

class EditProfileActivity : AppCompatActivity(),PermissionListener {
    lateinit var preferencesUsers: PreferencesUsers
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var filePath: Uri
    lateinit var mdb:DatabaseReference
    lateinit var iPassword:String
    lateinit var iName:String
    lateinit var iEmail:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        mdb = FirebaseDatabase.getInstance().getReference("User").child(preferencesUsers.getValues("user").toString())
        storage = FirebaseStorage.getInstance()
        storageReference =storage.reference
        val users = intent.getParcelableExtra<User>("user")
        txt_name_edit.setText(users.nama)
        txt_email_edit.setText(users.email)
        txt_password_edit.setText(users.password)
        if (preferencesUsers.getValues("url")?.isEmpty()!!){
            img_edit_profile.setImageResource(R.drawable.user_pic)
        }else{
            Glide.with(this).load(preferencesUsers.getValues("url"))
                .apply(RequestOptions.circleCropTransform()).into(img_edit_profile)
        }
        preferencesUsers.getValues("name")
        img_button_edit_profile.setOnClickListener {
            ImagePicker.with(this)
                .crop(1f, 1f)               //Crop Square image(Optional)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        save_profile.setOnClickListener {
            iPassword = txt_password_edit.text.toString()
            iName = txt_name_edit.text.toString()
            iEmail = txt_email_edit.text.toString()
            if (iPassword.equals("")){
                txt_password_edit.error = "Password Kosong"
                txt_password_edit.requestFocus()
            }else if(iName.equals("")){
                txt_name_edit.error = "Nama Kosong"
                txt_name_edit.requestFocus()
            }else if(iEmail.equals("")){
                txt_email_edit.error = "Email Kosong"
                txt_email_edit.requestFocus()
            }else{
                saveProfile(iName,iEmail,iPassword)
            }
        }
    }

    private fun saveProfile(iName: String, iEmail: String, iPassword: String) {
        mdb.child("nama").setValue(iName)
        preferencesUsers.setValues("nama",iName)
        mdb.child("email").setValue(iEmail)
        preferencesUsers.setValues("email",iEmail)
        mdb.child("password").setValue(iPassword)
        preferencesUsers.setValues("password",iPassword)
        Snackbar.make(save_profile,"Saved Profile",Snackbar.LENGTH_LONG).show()
    }


    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .cameraOnly()
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa mengedit photo profile", Toast.LENGTH_LONG ).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            filePath = data?.data!!
            Glide.with(this).load(filePath).apply(RequestOptions.circleCropTransform()).into(img_edit_profile)
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading..")
            progressDialog.show()

            Log.v("upload","File uri upload 2"+filePath)
            val fileName = UUID.randomUUID().toString()
            val ref =storageReference.child("images/"+fileName)
            mdb.child("image").setValue(fileName)
            ref.putFile(filePath)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    progressDialog.setMessage("Saving  Profile ...")
                    ref.downloadUrl.addOnSuccessListener {
                        preferencesUsers.setValues("url",it.toString())
                        mdb.child("url").setValue(it.toString())
                        Log.v("upload","url"+it.toString())
                        preferencesUsers.setValues("image",fileName)
                        return@addOnSuccessListener
                    }

                }
                .addOnFailureListener{e->
                    progressDialog.dismiss()
                    Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {taskSnapshot ->
                    val progress =100.0 *taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressDialog.setMessage("Saving  Profile ...")
                }
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Task Cancelled",Toast.LENGTH_LONG).show()
        }
    }
}
