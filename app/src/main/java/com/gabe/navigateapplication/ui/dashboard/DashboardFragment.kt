package com.gabe.navigateapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabe.navigateapplication.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var recyclerViewAdapter: RecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()
        initViewModel()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            val decoration = DividerItemDecoration(this@DashboardFragment.activity?.applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = RecyclerViewAdapter()
            //Footer not showing with Paging 3
            //https://stackoverflow.com/questions/67219182/footer-not-showing-with-paging-3
//My issue was that I was not setting the ConcatAdapter returned by withLoadStateFooter
//            After these
//                    yourAdapter.addLoadStateListener{}
//            val contcatAdapter = yourAdapter.withLoadStateFooter(footer = YourLoadStateFooter{rourAdapter.retry()}),
//            you just do rourRecyclerView.adapter = concatAdapter –
            val adapterWithLoading = recyclerViewAdapter.withLoadStateHeaderAndFooter(
//                header = PagingLoadStateAdapter( recyclerViewAdapter ),
//                footer = PagingLoadStateAdapter(recyclerViewAdapter )
                header = StateAdapter { recyclerViewAdapter.retry() },
                footer = StateAdapter { recyclerViewAdapter.retry() }
            )

            adapter = adapterWithLoading

        }

    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[RecyclerViewViewModel::class.java]
        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}