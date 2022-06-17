package com.gabe.navigateapplication.ui.dashboard

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gabe.navigateapplication.network.CharacterData
import com.gabe.navigateapplication.network.RetroService
import com.gabe.navigateapplication.pagingsource.CharacterPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecyclerViewViewModel @Inject constructor(
    private val retroService: RetroService,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    //没用注入时，用这个实例来完成网络请求； 有注入时有构造里的。  放在构造里解藕。
//    var retroService: RetroService = RetroInstance.getRetroInstance().create(RetroService::class.java)

    fun loadData() {
        val id = savedStateHandle[PAGING_ID_KEY] ?: 0
        viewModelScope.launch {
            Log.i("gabe", "paging ix :  $id")
            val response = retroService.getDataFromApi(id)
            // Handle response
            val pageInfo = response.info?.pages
            val namexx = response.results?.get(0)?.name
            Log.i("gabe", "paging ix :  $id || $pageInfo $namexx")
        }
    }

    private var pageDataFlow: Flow<PagingData<CharacterData>>? = null

//    // must be inside of the ViewModel class!
//    @AssistedFactory
//    interface Factory : AssistedSavedStateViewModelFactory<RecyclerViewViewModel> {
//        override fun create(savedStateHandle: SavedStateHandle): RecyclerViewViewModel  // may be ommited prior kotlin 1.3.60 or after PR #121 in AssistedInject lib
//    }

    // Keep the key as a constant
    companion object {
        private val PAGING_ID_KEY = "paging_ix_key"
    }


    fun saveCurrentId(pagingId: Int) {
        // Sets a new value for the object associated to the key.
        savedStateHandle.set(PAGING_ID_KEY, pagingId)
    }

    fun getCurrentId(): Int {
        // Gets the current value of the user id from the saved state handle
        return savedStateHandle.get(PAGING_ID_KEY) ?: 0
    }


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

//    class RecyclerViewModelFactory(
//        private val retroService: RetroService,
//        private val stateHandle: SavedStateHandle
//    ) : ViewModelProvider.NewInstanceFactory() {
//
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return RecyclerViewViewModel(retroService, stateHandle) as T
//        }
//
//    }
}