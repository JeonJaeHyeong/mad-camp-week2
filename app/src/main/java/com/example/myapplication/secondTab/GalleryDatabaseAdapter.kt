package com.example.myapplication.secondTab

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.gallery_item.view.*

class GalleryDatabaseAdapter(private val list:List<Image>) : RecyclerView.Adapter<GalleryDatabaseAdapter.GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gallery_item,
            parent, false)
        return GalleryViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int){
        val currentItem = list[position]
        val listener = View.OnClickListener { it->
            val intent = Intent(it.context, GalleryDatabaseShowActivity::class.java)
            intent.putExtra("File", currentItem.file)
            intent.putExtra("Name", currentItem.name)
            currentItem.file?.let { it1 -> Log.d("CHECK", it1) }
            it.context.startActivity(intent)
        }
        holder.apply{
            Log.d("holder.apply", list.size.toString())
            bind(listener, currentItem)
            itemView.tag = currentItem
        }
    }

    override fun getItemCount() = list.size

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var view: View = itemView
        fun bind(listener : View.OnClickListener, item:Image){
            item.name?.let { Log.d("in veiw holder", it) }
            item.file?.let { Log.d("in veiw holder", it) }

            var dString = android.util.Base64.decode(item.file, android.util.Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(dString, 0, dString.size)
            view.thumbnail.setImageBitmap(decodedImage)
            view.setOnClickListener(listener)
        }
    }
}