package com.example.myapplication.secondTab

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.retrofitTab.GalleryService
import kotlinx.android.synthetic.main.activity_gallery_database_show.*
import kotlinx.android.synthetic.main.activity_gallery_show.expanded_image
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream

class GalleryDatabaseShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_database_show)

        val bundle: Bundle = intent.extras!!
        val file = bundle.getString("File")
        val filename = bundle.getString("Name")
        val idx = bundle.getInt("Idx")
        var dString = android.util.Base64.decode(file, android.util.Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(dString, 0, dString.size)
        expanded_image.setImageBitmap(decodedImage)

        //setContentView()

        btn_download.setOnClickListener{
            try {
                val iStream = ByteArrayInputStream(dString)
                var image = BitmapFactory.decodeStream(iStream)
                MediaStore.Images.Media.insertImage(contentResolver, image, "Title", null)
            }catch(e : Exception){
                Log.v("SaveFile",""+e)
            }
        }

//        val bundle: Bundle = intent.extras!!
//        val file = bundle.getString("File")
//        val filename = bundle.getString("Name")
//        var dString = android.util.Base64.decode(file, android.util.Base64.DEFAULT)
//        val decodedImage = BitmapFactory.decodeByteArray(dString, 0, dString.size)
//        expanded_image.setImageBitmap(decodedImage)
//
//        //setContentView()
//
//        btn_download.setOnClickListener{
//            var retrofit = Retrofit.Builder()
//                .baseUrl("http://192.249.18.217:5000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            var galleryService: GalleryService = retrofit.create(GalleryService::class.java)
//        }
    }
}