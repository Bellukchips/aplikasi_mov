package com.belluk.movapps.fragment

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.belluk.movapps.R
import com.belluk.movapps.activity.EditProfileActivity
import com.belluk.movapps.activity.HomeScreenActivity
import com.belluk.movapps.activity.MyWalletActivity
import com.belluk.movapps.activity.SignInActivity
import com.belluk.movapps.model.User
import com.belluk.movapps.utils.PreferencesUsers
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var mProfileFile: File? = null
    var statusAdd: Boolean = false
    lateinit var filePath: Uri
    lateinit var builder: AlertDialog.Builder
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferencesUsers: PreferencesUsers
    private val REQUEST_IMAGE_CAPTURE = 100
    lateinit var mdb:DatabaseReference
    var dataList = ArrayList<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = view.findViewById<TextView>(R.id.user_profile_name)
        val email = view.findViewById<TextView>(R.id.email_profile)
        val edit = view.findViewById<TextView>(R.id.edit_profile)
        val logout = view.findViewById<TextView>(R.id.logout)
        val profile = view.findViewById<ImageView>(R.id.user_profile_iv)
        preferencesUsers = PreferencesUsers(activity!!.applicationContext)
        name.text = preferencesUsers.getValues("nama")
        email.text = preferencesUsers.getValues("email")
        val user = User()
        user.nama = preferencesUsers.getValues("nama")
        user.password = preferencesUsers.getValues("password")
        user.email = preferencesUsers.getValues("email")
        my_wallet.setOnClickListener {
            startActivity(Intent(context,MyWalletActivity::class.java))
        }
        edit.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java).putExtra("user",user))
        }
        logout.setOnClickListener {
            preferencesUsers.setValues("status", "0")
            val i = Intent(context, SignInActivity::class.java)
            startActivity(i)
            activity!!.finishAffinity()
        }
        language.setOnClickListener {
            val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(i)
        }
        if (preferencesUsers.getValues("url")?.isEmpty()!!){
            profile.setImageResource(R.drawable.user_pic)
        }else{
            Glide.with(this).load(preferencesUsers.getValues("url"))
                .apply(RequestOptions.circleCropTransform()).into(profile)
        }
    }
}
