package com.gabe.navigateapplication.ui.home


import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabe.navigateapplication.GabeDatabase
import com.gabe.navigateapplication.ItemInCart
import com.gabe.navigateapplication.R
import com.gabe.navigateapplication.databinding.FragmentHomeBinding
import com.squareup.sqldelight.android.AndroidSqliteDriver


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val rocketImage: ImageView = binding.rocketImage
        rocketImage.setImageResource( R.drawable.rocket_thrust)
        rocketAnimation = rocketImage.drawable as AnimationDrawable
        rocketAnimation.start()


        val androidSqlDriver = AndroidSqliteDriver(
            schema = GabeDatabase.Schema,
            context = this@HomeFragment.requireContext(),
            name = "items.databases"
        )

        val queries = GabeDatabase(androidSqlDriver).itemInCartEntityQueries

        val itemsBefore: List<ItemInCart> = queries.selectAll().executeAsList()
        Log.d("ItemDatabase", "Items Before: $itemsBefore")

        for (i in 1..3) {
            queries.insertOrReplace(
                label = "Item $i",
                image = "https://localhost/item$i.png",
                quantity = i.toLong(),
                link = null
            )
        }

        val itemsAfter: List<ItemInCart> = queries.selectAll().executeAsList()
        Log.d("ItemDatabase", "Items After: $itemsAfter")


        return root
    }
    lateinit var  rocketAnimation  :AnimationDrawable


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}