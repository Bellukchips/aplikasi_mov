package com.belluk.movapps.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.belluk.movapps.R
import com.belluk.movapps.utils.PreferencesUsers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_photoscreen.*
import java.util.*

class RegisterPhotosScreenActivity : AppCompatActivity(), PermissionListener {
    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath:Uri
    lateinit var builder :AlertDialog.Builder
    lateinit var storage:FirebaseStorage
    lateinit var mdb:DatabaseReference
    lateinit var storageReference: StorageReference
    lateinit var preferencesUsers: PreferencesUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_photoscreen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        storage = FirebaseStorage.getInstance()
        storageReference =storage.reference
        txt_hello.text = intent.getStringExtra("nama")
        mdb = FirebaseDatabase.getInstance().getReference("User").child(preferencesUsers.getValues("user").toString())
        iv_add.setOnClickListener {
            if (statusAdd){
                statusAdd = false
                btn_save_img.visibility = View.INVISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                img_picture_upload.setImageResource(R.drawable.user_pic)
            }else{
                ImagePicker.with(this)
                    .crop(1f, 1f)               //Crop Square image(Optional)
                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }
        }
        btn_upload_later.setOnClickListener {
            finishAffinity()
            val intent = Intent(this@RegisterPhotosScreenActivity,HomeScreenActivity::class.java)
            startActivity(intent)
        }
        btn_save_img.setOnClickListener {
            if (filePath !=null){
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
                        Toast.makeText(this,"Uploaded",Toast.LENGTH_LONG).show()
                        ref.downloadUrl.addOnSuccessListener {
                            preferencesUsers.setValues("url",it.toString())
                            mdb.child("url").setValue(it.toString())
                            Log.v("upload","url"+it.toString())
                            preferencesUsers.setValues("image",fileName)
                            finishAffinity()
                            val intent = Intent(this,HomeScreenActivity::class.java)
                            startActivity(intent)
                        }

                    }
                    .addOnFailureListener{e->
                        progressDialog.dismiss()
                        Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {taskSnapshot ->
                        val progress =100.0 *taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Wait Processing Upload ...")
                    }
            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .cameraOnly()
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG ).show()
    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Tergesah Upload Foto ? Upload Nanti Saja")
            .setCancelable(false)
            .setNegativeButton(
                "Ya"
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            statusAdd =true
            filePath = data?.data!!
            Glide.with(this).load(filePath).apply(RequestOptions.circleCropTransform()).into(img_picture_upload)
            btn_save_img.visibility =View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)
        }else if(resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this,ImagePicker.getError(data),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Task Cancelled",Toast.LENGTH_LONG).show()
        }
    }
}
