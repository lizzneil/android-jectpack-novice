package com.gabe.navigateapplication.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gabe.navigateapplication.R
import com.gabe.navigateapplication.network.CharacterData


class RecyclerViewAdapter :
    PagingDataAdapter<CharacterData, RecyclerViewAdapter.MyViewHolder>(DiffUtilCallback()) {
    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {
        Log.i(
            "gabe-rc-adapter",
            "${getItem(position)?.name}:$position \t image: ${getItem(position)?.image}"
        )
//        2022-06-08 01:01:24.832 5466-5466/com.gabe.pagingv3demo I/gabe-rc-adapter: Big Boobed Waitress:40
//        image: https://rickandmortyapi.com/api/character/avatar/41.jpeg
//        2022-06-08 01:01:24.842 5466-5466/com.gabe.pagingv3demo I/gabe-rc-adapter: null:39 	 image: null
        //滚快了会出现空，原因不明，待查。
//　官方文档说这里可能空。。。。。
        //https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data
        //Define a RecyclerView adapter
        // Note that item may be null. ViewHolder must support binding a
        // null item as a placeholder.
        //holder?.bind(getItem(position)!!)
//找到一个空指针的原因，并简单验证了一下。PagingConfig 限制了最大小。如果超了就会把前面的丢掉。如果不限制就会占内存。
        val user: CharacterData? = getItem(position)
        if (user != null) {
            holder?.bind(user)
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database

            holder.placeholder()
//            holder.itemView.invalidate();
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.MyViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_row_item_layout, parent, false)
        return MyViewHolder(inflater)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView: ImageView = view.findViewById<ImageView>(R.id.header_image_view)
        private val tvName: TextView = view.findViewById<TextView>(R.id.tvName)
        private val tvDesc: TextView = view.findViewById<TextView>(R.id.tvDesc)
        fun bind(data: CharacterData) {
            tvName.text = data.name
            tvDesc.text = data.species
            Glide.with(imageView).load(data.image).circleCrop().into(imageView)
        }

        fun placeholder() {
            tvName.text = "notice"
            tvDesc.text = "loading... "
            imageView.setImageResource(R.drawable.ic_launcher_foreground)
//            if(data?image!!){
//
//            }
//            imageView.load(data: CharacterData?.uimagerl) {
//                placeholder(R.drawable.ic_launcher_foreground)
//            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<CharacterData>() {
        override fun areItemsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
            return oldItem.name == newItem.name && oldItem.species == oldItem.species && oldItem.image == newItem.image
        }
    }
}