package com.belluk.movapps.activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.belluk.movapps.R
import com.belluk.movapps.adapter.SeatAdapter
import com.belluk.movapps.model.Seat
import com.belluk.movapps.model.Tiket
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.item_rectangle_blue.*
import java.util.*
import kotlin.collections.ArrayList


class DetailTiketActivity : AppCompatActivity() {
lateinit var builder :AlertDialog.Builder
     var dataList = ArrayList<Seat>()
    lateinit var mDb:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val rc_seat = findViewById<RecyclerView>(R.id.rc_seat_tiket_detail)
        val data = intent.getParcelableExtra<Tiket>("data")
        mDb = FirebaseDatabase.getInstance().getReference("Tiket").child(data.user.toString()).child(data.kodeTiket.toString()).child("kursi")
        tv_title_tiket_detail.text = data.nama
        tv_genre_tiket.text = data.genre
        title = "Ticket Code : "+data.kodeTiket
        Glide.with(this).load(data.poster).into(iv_poster_image_tiket)
        tv_rate_tiket_detail.text = data.rating
        tv_date_tiket.text = data.date
        nama_bioskop_tiket_detail.text = data.bioskop+","+data.cinema
        tv_time_show.text = data.timer
        rc_seat.layoutManager = LinearLayoutManager(this)

        //generate QR code
        val text=data.kodeTiket // Whatever you need to encode in the QR code
        val multiFormatWriter =  MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,600,600);
            val barcodeEncoder =  BarcodeEncoder()
            val bitmap:Bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QrBarcode.setImageBitmap(bitmap)
        } catch (e:WriterException) {
            e.printStackTrace();
        }
        //generate 1D barcode
        try{
            val txt = data.kodeTiket
            val hintMap = Hashtable<EncodeHintType,ErrorCorrectionLevel>()
            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
            var codeWriter: Writer
            codeWriter = Code128Writer()
            val bitMatrix:BitMatrix = codeWriter.encode(txt,BarcodeFormat.CODE_128,260,50,hintMap)
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
            img_barcode.setImageBitmap(bitmap)
        }catch (e:Exception){
            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
        }
        //alert dialog
        QrBarcode.setOnClickListener {
            builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_qr_layout,null)
            builder.setView(dialogView)
            builder.setCancelable(true)
            val cancel = dialogView.findViewById(R.id.btn_close) as Button
            val img = dialogView.findViewById(R.id.dialog_image) as ImageView
            val dialog = builder.create()
            //generate QR code
            val text=data.kodeTiket // Whatever you need to encode in the QR code
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
        getData()
    }
    private fun getData(){
        val rc_seat = findViewById<RecyclerView>(R.id.rc_seat_tiket_detail)
        mDb.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@DetailTiketActivity,""+p0.message,Toast.LENGTH_LONG).show()
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
