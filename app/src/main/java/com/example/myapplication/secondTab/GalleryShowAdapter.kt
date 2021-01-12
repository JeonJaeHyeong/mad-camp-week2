package com.example.myapplication.secondTab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.gallery_show_item.view.*

class GalleryShowAdapter(private val galleryShowList: ArrayList<GalleryItem>) : RecyclerView.Adapter<GalleryShowAdapter.GalleryShowViewHolder>() {

    interface  ItemClick {
        fun onClick(view: View, item: GalleryItem)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryShowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_show_item,
            parent, false
        )
        return GalleryShowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GalleryShowViewHolder, position: Int) {
        val currentItem = galleryShowList[position]

        val listener = View.OnClickListener { it ->
        }
        holder.apply {
            setItem(listener, currentItem)
            itemView.tag = currentItem
        }
        if(itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, currentItem)
            }
        }
    }

    override fun getItemCount() = galleryShowList.size

    class GalleryShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setItem(listener: View.OnClickListener, item: GalleryItem) {
            itemView.show_view.setImageURI(item.imageResource)
            itemView.setOnClickListener(listener)
        }
    }
}
