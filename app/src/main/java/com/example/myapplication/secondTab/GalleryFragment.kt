package com.example.myapplication.secondTab

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.facebook.AccessToken
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GalleryFragment : Fragment() {
    val FLAG_PERM_CAMERA = 98
    val FLAG_REQ_CAMERA = 101

    val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open) }
    val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close) }
    val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom) }
    val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom) }
    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        setContentView(view)
        view.fab.setOnClickListener { openCamera() }

        view.option_addG.setOnClickListener {
            onAddButtonClicked()
            setContentView(view)
        }

        view.option_dbG.setOnClickListener{
            if(AccessToken.getCurrentAccessToken()!=null){
                val intent = Intent(context, GalleryDbActivity::class.java)
                context?.startActivity(intent)
            }else{
                Toast.makeText(context, "로그인을 해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun setContentView(view: View){
        view.gallery_recycler_view.adapter =
            GalleryAdapter(getFileList(requireContext(), MediaStoreFileType.IMAGE))
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, FLAG_REQ_CAMERA)
    }

    private fun saveImageFile(bitmap: Bitmap, title: String) : Uri? {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            bitmap,
            title,
            "image of $title"
        )
        return Uri.parse(savedImageURL)
    }
    private fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return filename
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                FLAG_REQ_CAMERA -> {
                    if (data?.extras?.get("data") != null) {
                        val bitmap = data.extras?.get("data") as Bitmap
                        val filename = newFileName()

                        val uri = saveImageFile(bitmap, filename)
                        //imagePreview.setImageURI(uri)
                    }
                }
            }
//            val ft = requireFragmentManager().beginTransaction()
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false)
//            }
//            ft.detach(this).attach(this).commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            FLAG_PERM_CAMERA -> {
                var checked = true
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        checked = false
                        break
                    }
                }
                if (checked) { openCamera() }
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
//                Log.d("image", id.toString())
//                Log.d("image", dateTaken.toString())
//                Log.d("image", displayName.toString())
//                Log.d("image", bucketID.toString())
//                Log.d("image", bucketName.toString())
                imageList.add(GalleryItem(contentUri, displayName.toString()))
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


    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            option_dbG.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
        }else {
            option_dbG.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            option_dbG.startAnimation(fromBottom)
            fab.startAnimation(fromBottom)
            option_addG.startAnimation(rotateOpen)
        }else {
            option_dbG.startAnimation(toBottom)
            fab.startAnimation(toBottom)
            option_addG.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean){
        if(!clicked){
            option_dbG.isClickable = true
            fab.isClickable = true
        }else {
            option_dbG.isClickable = false
            fab.isClickable = false
        }
    }
}