package com.belluk.movapps.activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.adapter.MyTiketAdapter
import com.belluk.movapps.adapter.SeatAdapter
import com.belluk.movapps.adapter.TiketAdapter
import com.belluk.movapps.model.*
import com.belluk.movapps.utils.PreferencesUsers
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_tiket.*
import kotlinx.android.synthetic.main.activity_tiket.iv_poster_image_tiket
import kotlinx.android.synthetic.main.activity_tiket.nama_bioskop_tiket_detail
import kotlinx.android.synthetic.main.activity_tiket.tv_date_tiket
import kotlinx.android.synthetic.main.activity_tiket.tv_genre_tiket
import kotlinx.android.synthetic.main.activity_tiket.tv_rate_tiket_detail
import kotlinx.android.synthetic.main.activity_tiket.tv_title_tiket_detail
import kotlinx.android.synthetic.main.item_rectangle_blue.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TiketActivity : AppCompatActivity() {
    private var dataList = ArrayList<Seat>()
    lateinit var preferencesUsers:PreferencesUsers
    private var dateFormat: SimpleDateFormat? = null
    private var date: String? = null
    lateinit var builder : AlertDialog.Builder
    lateinit var mDb :DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preferencesUsers = PreferencesUsers(this)
        val rc_seat = findViewById<RecyclerView>(R.id.rc_checkout)
        val mytiket = intent.getParcelableExtra<Tiket>("mytiket")
        val dateGet = intent.getStringExtra("date")
        mDb = FirebaseDatabase.getInstance().getReference("Tiket").child(mytiket.user.toString()).child(mytiket.kodeTiket.toString()).child("kursi")
        tv_title_tiket_detail.text = mytiket.nama
        tv_genre_tiket.text = mytiket.genre
        tv_rate_tiket_detail.text = mytiket.rating
        nama_bioskop_tiket_detail.text = mytiket.bioskop+" , "+mytiket.cinema
        rc_seat.layoutManager = LinearLayoutManager(this)
        rc_seat.adapter = SeatAdapter(dataList){
        }
        Glide.with(this).load(mytiket.poster).into(iv_poster_image_tiket)
//        dateFormat = SimpleDateFormat("EEE, MMM dd YYYY")
//        date = dateFormat!!.format(Date())
        tv_date_tiket.text = dateGet
        //generate QR code
        val text= mytiket.kodeTiket // Whatever you need to encode in the QR code
        val multiFormatWriter =  MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,50,50);
            val barcodeEncoder =  BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_qr.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace();
        }
        //generate 1D barcode
        try{
            val txt = mytiket.kodeTiket
            val hintMap = Hashtable<EncodeHintType,ErrorCorrectionLevel>()
            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
            var codeWriter: Writer
            codeWriter = Code128Writer()
            val bitMatrix:BitMatrix = codeWriter.encode(txt,BarcodeFormat.CODE_128,350,60,hintMap)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap:Bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
            for (i in 0 until width) {
                for (j in 0 until height) {
                    bitmap.setPixel(
                        i,
                        j,
                        if (bitMatrix.get(
                                i,
                                j
                            )
                        ) Color.BLACK else Color.WHITE
                    )
                }
            }
            img_barcode_tiket.setImageBitmap(bitmap)
        }catch (e:Exception){
            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
        }
        getData()
        img_qr.setOnClickListener {
            builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_qr_layout,null)
            builder.setView(dialogView)
            builder.setCancelable(true)
            val cancel = dialogView.findViewById(R.id.btn_close) as Button
            val img = dialogView.findViewById(R.id.dialog_image) as ImageView
            val dialog = builder.create()
            //generate QR code
            val text=mytiket.kodeTiket // Whatever you need to encode in the QR code
            val multiFormatWriter =  MultiFormatWriter()
            try {
                val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
                val barcodeEncoder =  BarcodeEncoder()
                val bitmap:Bitmap = barcodeEncoder.createBitmap(bitMatrix);
                img.setImageBitmap(bitmap)
            } catch (e:WriterException) {
                e.printStackTrace();
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }
    private fun getData(){
        val rc_seat = findViewById<RecyclerView>(R.id.rc_checkout)
        mDb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@TiketActivity,""+p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnap: DataSnapshot) {
                dataList.clear()
                for(i in dataSnap.children){
                    val seat = i.getValue(Seat::class.java)
                    dataList.add(seat!!)
                }
                if (dataList.isNotEmpty()){
                    rc_seat.adapter = SeatAdapter(dataList){}
                }
            }

        })
    }
}
