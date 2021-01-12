package com.example.myapplication.firstTab

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.contact_item.view.*

class PhoneDatabaseAdapter(private val list: List<Phone>) : RecyclerView.Adapter<PhoneDatabaseAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item,
            parent, false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val phone = list[position]

        val listener = View.OnClickListener { it ->
            val intent = Intent(it.context, PhoneDbShowActivity::class.java)
            intent.putExtra("ProfileImage", R.drawable.ic_human)
//          여기서 잠깐 프로필사진에 컬러필터 뭐 씌울지 저장
            var num = 0
            var colorFilter = "";
            try {
                num = Character.getNumericValue(phone.phone.toString().last())
            } catch (nfe: NumberFormatException) {
                // not a valid int
            }
            colorFilter = when(num % 3) {
                0 -> "#F1C400"
                1 -> "#F59D00"
                else -> "#92CDED"
            }
//
            intent.putExtra("ColorFilter", colorFilter)
            intent.putExtra("Name", phone.name)
            intent.putExtra("PhoneNumber", phone.phone)

            it.context.startActivity(intent)
        }

        holder.apply{
            setPhone(listener, phone)
            itemView.tag = phone
        }

    }
    override fun getItemCount() = list.size

    @SuppressLint("MissingPermission")
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPhone: Phone? = null
        fun setPhone(listener: View.OnClickListener, phone: Phone) {
            this.mPhone = phone
            var num = 0
            try {
                num = Character.getNumericValue(phone.phone.toString().last())
            } catch (nfe: NumberFormatException) {
                // not a valid int
            }
            when(num % 3) {
                0 -> itemView.image_view.setColorFilter(Color.parseColor("#F1C400")) //sunflower
                1 -> itemView.image_view.setColorFilter(Color.parseColor("#F59D00")) //orange
                else -> itemView.image_view.setColorFilter(Color.parseColor("#92CDED")) //sky_blue
            }
            itemView.textName.text = phone.name
            itemView.textPhone.text = phone.phone
            itemView.setOnClickListener(listener)
        }
    }
}
