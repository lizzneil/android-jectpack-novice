package com.gabe.navigateapplication.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gabe.navigateapplication.network.CharacterData
import com.gabe.navigateapplication.network.RetroService
import com.gabe.navigateapplication.pagingsource.CharacterPagingSource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow


@ActivityScoped
class RecyclerViewViewModel(private val retroService: RetroService) : ViewModel() {
//    var retroService: RetroService = RetroInstance.getRetroInstance().create(RetroService::class.java)

//    private lateinit var pageDataFlow  :Flow<PagingData<CharacterData>>


    //    fun loadData() {
//        val id = handle["id"] ?: "default"
//        viewModelScope.launch {
//            val response = retroService.getDataFromApi(id)
//            // Handle response
//        }
//    }
//    private lateinit var pageDataFlow  : MutableSharedFlow<PagingData<CharacterData>>
    private var pageDataFlow: Flow<PagingData<CharacterData>>? = null

    fun getListData(): Flow<PagingData<CharacterData>> {
        //避免每次都创建新的实 例，导 致回不到 上次加载的状态。
        val lastResult = pageDataFlow
        if (lastResult != null) {
            return lastResult
        }
        pageDataFlow = Pager(
            //这里不要限制最大数，当记录超最大数时，超的会空指针。除非你有把制握不超。
            config = PagingConfig(pageSize = 20),//PagingConfig(pageSize = 34, maxSize = 200),
            pagingSourceFactory = { CharacterPagingSource(retroService) }
        ).flow.cachedIn(viewModelScope)

        return pageDataFlow as Flow<PagingData<CharacterData>>
    }
}