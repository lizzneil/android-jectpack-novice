package com.gabe.navigateapplication.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabe.navigateapplication.R
import com.gabe.navigateapplication.databinding.loadImage
import com.gabe.navigateapplication.network.CharacterData


class RecyclerViewAdapter :
    PagingDataAdapter<CharacterData, RecyclerViewAdapter.MyViewHolder>(DiffUtilCallback()) {


    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {
        Log.i(
            "gabe-rc-adapter",
            "${getItem(position)?.name}:$position \t image: ${getItem(position)?.image}"
        )
//        com.gabe.pagingv3demo I/gabe-rc-adapter: Big Boobed Waitress:40
//        image: https://rickandmortyapi.co要m/api/character/avatar/41.jpeg
//        com.gabe.pagingv3demo I/gabe-rc-adapter: null:39 	 image: null
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

        private var data: CharacterData? = null

        init {
            view.setOnClickListener {
                data?.let { personDetail ->
                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle("Android Alert")
                    builder.setMessage("${personDetail.name}    @ ${personDetail.species}")
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        Toast.makeText(
                            view.context,
                            android.R.string.yes, Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            view.context,
                            android.R.string.no, Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.setNeutralButton("Maybe") { dialog, which ->
                        Toast.makeText(
                            view.context,
                            "Maybe   ${personDetail.name}    @ ${personDetail.species}", Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()
                }
            }
        }

        fun bind(aData: CharacterData?) {
            if (aData == null) {
                placeholder()
            } else {
                showRepoData(aData)
            }
        }

        private fun showRepoData(repo: CharacterData) {
            data = repo

            tvName.text = data!!.name
            tvDesc.text = data!!.species
            //用配置好的公用glide 替换现在的。一改全改。
            loadImage(imageView, data!!.image!!)
//            Glide.with(imageView).load(data.image).circleCrop().into(imageView)
        }

        fun placeholder() {
            tvName.text = "notice"
            tvDesc.text = "loading... "
            imageView.setImageResource(R.drawable.ic_launcher_foreground)
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