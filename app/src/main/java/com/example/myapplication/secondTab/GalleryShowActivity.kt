package com.example.myapplication.secondTab

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.retrofitTab.GalleryService
import com.example.myapplication.retrofitTab.ImageInf
import com.facebook.AccessToken
import kotlinx.android.synthetic.main.activity_gallery_show.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class GalleryShowActivity : AppCompatActivity() {
    var imageInf: ImageInf? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {           ////Geonhee
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_show)

        val bundle: Bundle = intent.extras!!
        val uri = Uri.parse(bundle.getString("Uri"))
        val filename = bundle.getString("Name")
        expanded_image.setImageURI(uri)

        setContentView()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.249.18.225:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var galleryService: GalleryService = retrofit.create(GalleryService::class.java)

        btn_upload.setOnClickListener{
            if (filename != null) {

                //var loginCallback = LoginCallback()
                val accessToken = AccessToken.getCurrentAccessToken()
                if (accessToken != null) {
                    var iStream = contentResolver.openInputStream(uri)
                    val image = iStream?.let { getImageContent(it) }
                    val encodedImage = Base64.getEncoder().encodeToString(image)
                    Log.d("Sending Image", "START")

                    galleryService.imageUpload(filename, encodedImage)
                        .enqueue(object : Callback<ImageInf> {
                            override fun onFailure(call: Call<ImageInf>, t: Throwable) {
                                Log.d("fail", t.toString())
                                var dialog = AlertDialog.Builder(this@GalleryShowActivity)
                                dialog.setMessage("호출 실패했습니다.")
                                dialog.show()
                            }

                            override fun onResponse(
                                call: Call<ImageInf>,
                                response: Response<ImageInf>
                            ) {
                                Log.d("success", response.toString())
                                imageInf = response.body()
                                var dialog = AlertDialog.Builder(this@GalleryShowActivity)
                                dialog.setMessage("업로드 성공!")
                                dialog.show()
                            }
                        })
                }
                else{
                    Toast.makeText(this, "로그인을 해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Throws(IOException::class)
    fun getImageContent(inputStream: InputStream): ByteArray? {     ////// Geonhee
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val filePath: String?
        val cursor: Cursor? =
            this.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            filePath = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(idx)
            cursor.close()
        }
        return filePath
    }


    private fun setContentView() {
        val mAdapter = GalleryShowAdapter(getFileList(this, MediaStoreFileType.IMAGE))
        //val mAdapter = GalleryShowAdapter(generateDummyImage(40))
        show_recycler.adapter = mAdapter
        show_recycler.layoutManager = LinearLayoutManager(this)

        mAdapter.itemClick = object: GalleryShowAdapter.ItemClick {
            override fun onClick(view: View, item: GalleryItem) {
                expanded_image.setImageURI(item.imageResource)
            }
        }
    }
    private fun getFileList(context: Context, type: MediaStoreFileType): ArrayList<GalleryItem> {
        val imageList = ArrayList<GalleryItem>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_TAKEN,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Files.FileColumns.BUCKET_ID
        )
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_TAKEN} DESC"
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dateTakenColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val bucketIDColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    type.externalContentUri,
                    id.toString()
                )
                val bucketID = cursor.getLong(bucketIDColumn)
                val bucketName = cursor.getString(bucketNameColumn)
                imageList.add(GalleryItem(contentUri, "image"))
            }
        }
        return imageList
    }

    enum class MediaStoreFileType(
        val externalContentUri: Uri,
        val mimeType: String,
        val pathByDCIM: String
    ) {
        IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*", "/image"),
    }
}
