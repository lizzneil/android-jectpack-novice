package com.gabe.navigateapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabe.navigateapplication.R
import com.gabe.navigateapplication.databinding.FragmentDashboardBinding
import com.gabe.navigateapplication.util.visibleWhen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private val viewModel: RecyclerViewViewModel by viewModels<RecyclerViewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        binding.rcRetryButton .setOnClickListener {
            recyclerViewAdapter.retry()
        }
//        movie_retry_button.setOnClickListener {
//            movieAdapter.retry()
//        }
        return root
    }


    //    Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
//    but before any saved state has been restored in to the view.
//    This gives subclasses a chance to initialize themselves once they know their view hierarchy has been completely created.
//    The fragment's view hierarchy is not however attached to its parent at this point.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            val decoration = DividerItemDecoration(
                this@DashboardFragment.activity?.applicationContext,
                DividerItemDecoration.VERTICAL
            )
            addItemDecoration(decoration)
            recyclerViewAdapter = RecyclerViewAdapter()
            //Footer not showing with Paging 3
            //https://stackoverflow.com/questions/67219182/footer-not-showing-with-paging-3
//            意思是 要返回加了 header footer的对象，不然白加了。没效果。
//My issue was that I was not setting the ConcatAdapter returned by withLoadStateFooter
//            After these
//                    yourAdapter.addLoadStateListener{}
//            val contcatAdapter = yourAdapter.withLoadStateFooter(footer = YourLoadStateFooter{rourAdapter.retry()}),
//            you just do rourRecyclerView.adapter = concatAdapter –
            val adapterWithLoading = recyclerViewAdapter.withLoadStateHeaderAndFooter(
                header = StateAdapter { recyclerViewAdapter.retry() },
                footer = StateAdapter { recyclerViewAdapter.retry() }
            )


            recyclerViewAdapter.addLoadStateListener { loadState ->

                loadState.refresh.let {
                    binding.rcErrorMsgTextView .visibleWhen(it is LoadState.Error)
                    binding.rcRetryButton .visibleWhen(it is LoadState.Error)
                    binding.rcProgressBar .visibleWhen(it is LoadState.Loading)
                    binding.recycleView.visibleWhen(it is LoadState.NotLoading)

                    if (it is LoadState.Error) {
                        binding.rcErrorMsgTextView.text =
                            it.error.localizedMessage
                    }
                }
            }
            adapter = adapterWithLoading

        }

    }

    private fun initViewModel() {
//初始化 viewModel    方 法1      无参的情况用这个  目标RecyclerViewViewModel无参  无参用默认的Factory就可以
//        val viewModel = ViewModelProvider(this)[RecyclerViewViewModel::class.java]
//        val viewModel2 = ViewModelProvider(this).get(modelClass = RecyclerViewViewModel::class.java)

        //以下是有参的情况 目标RecyclerViewViewModel有参  有参需要自定义Factory

//初始化 viewModel     方 法2      RetroApiModule .provideGithubApi(context)
//        val tRetroService = RetroInstance.getRetroInstance().create(RetroService::class.java)
//        val tFactory = RecyclerViewModelFactory(tRetroService)
//        val viewModel = ViewModelProviders.of(this, tFactory).get(modelClass = RecyclerViewViewModel::class.java)

        //初始化 viewModel     方 法3   不用管，HITL 注入的。 在类里面声明成员变量，指明 是注入的viewModel
        //这个是示例。
        // private val viewModel: RecyclerViewViewModel by viewModels<RecyclerViewViewModel>()
        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}