package com.example.myapplication.firstTab

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.retrofitTab.PhoneInf
import com.example.myapplication.retrofitTab.PhoneService
import kotlinx.android.synthetic.main.activity_phone_book_show.phone_show_image
import kotlinx.android.synthetic.main.activity_phone_book_show.phone_show_name
import kotlinx.android.synthetic.main.activity_phone_book_show.phone_show_phone_btn
import kotlinx.android.synthetic.main.activity_phone_book_show.phone_show_phone_number
import kotlinx.android.synthetic.main.activity_phone_db_show.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhoneDbShowActivity : AppCompatActivity() {

    var phoneInf: PhoneInf? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_db_show)

        val phoneBookShowImage: ImageView = phone_show_image
        val phoneBookShowName: TextView = phone_show_name
        val phoneBookShowNum: TextView = phone_show_phone_number

        val bundle: Bundle = intent.extras!!

        val profileImage = bundle.getInt("ProfileImage")
        val colorFilter = bundle.getString("ColorFilter")
        val name = bundle.getString("Name")
        val phoneNumber = bundle.getString("PhoneNumber")

        phoneBookShowImage.setImageResource(profileImage)
        phoneBookShowImage.setColorFilter(Color.parseColor(colorFilter))
        phoneBookShowName.text = name
        phoneBookShowNum.text = phoneNumber

        phone_show_phone_btn.setOnClickListener{
            val uri = Uri.parse("tel:${phoneNumber.toString()}")
            val intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        }

        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.249.18.225:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var phoneService: PhoneService = retrofit.create(PhoneService::class.java)


        phone_show_download.setOnClickListener {
            var text1 = name.toString()
            var text2 = phoneNumber.toString()

            val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
                type = ContactsContract.RawContacts.CONTENT_TYPE
            }
            intent.apply {
                // Inserts an email address
                putExtra(ContactsContract.Intents.Insert.NAME, text1)
                putExtra(
                    ContactsContract.Intents.Insert.EMAIL_TYPE,
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK
                )
                putExtra(ContactsContract.Intents.Insert.PHONE, text2)
                putExtra(
                    ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                )
            }
            startActivity(intent)
        }

        phone_show_delete.setOnClickListener{
            var text1 = name.toString()
            var text2 = phoneNumber.toString()
            phoneService.phoneDelete(text1,text2).enqueue(object: Callback<PhoneInf> {
                override fun onFailure(call: Call<PhoneInf>, t: Throwable) {
                    Log.d("fail", t.toString())
                    var dialog = AlertDialog.Builder(this@PhoneDbShowActivity)
                    dialog.setMessage("호출 실패했습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<PhoneInf>, response: Response<PhoneInf>) {
                    phoneInf = response.body()
                    var dialog = AlertDialog.Builder(this@PhoneDbShowActivity)
                    dialog.setMessage("삭제 성공!")
                    dialog.show()
                }
            })
        }
    }

}