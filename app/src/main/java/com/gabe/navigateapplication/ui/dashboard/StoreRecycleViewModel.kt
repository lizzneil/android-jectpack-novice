package com.gabe.navigateapplication.ui.dashboard

import android.util.Log
import androidx.hilt.lifecycle.ViewModelAssistedFactory
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

//https://proandroiddev.com/saving-ui-state-with-viewmodel-savedstate-and-dagger-f77bcaeb8b08#f2a2
//解释了 因历史原因。现在需要怎么做。
//You may have noticed that we have a problem now.
//
//First, we need to extend AbstractSavedStateViewModelFactory and not ViewModelProvider.Factory
//and second, the we have a new ViewModel constructor argument, SavedStateHandle,
// but we only get its instance when AbstractSavedStateViewModelFactory calls our implementation of create() function where it then passes the SavedStateHandle instance.
// With the traditional Dagger setup, we get the ViewModel instance from Provider<ViewModel>.get() and
// there’s no option to pass extra constructor parameters so this approach with Dagger is not going to work anymore.
// We’ve already seen earlier that the only way to solve this problem is by having a ViewModel specific factory.
//Moreover, AbstractSavedStateViewModelFactory takes SavedStateRegistryOwner,
// which if you recall, is a very important argument that associates the saved state to the Activity/Fragment.
// We cannot just inject whatever Activity/Fragment instance here.
//Unfortunately, the traditional approach is no longer valid when using SavedState ViewModel.
// As mentioned previously, you have to create one Factory per ViewModel while making sure that you pass the right SavedStateRegistryOwner instance.
//
//Nevertheless, we can still use Dagger to inject other dependencies needed for the ViewModel by creating 2 layers of factories — covered in the next section.

//被KILL 时存一下参数。用于恢复
//为存SavedStateHandle 写的。实际上不写也可以，直接用RecyclerViewViewModel。
//在非注入时只能用 这个。
@HiltViewModel
class StoreRecycleViewModel @Inject constructor(
    private val retroService: RetroService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


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


    fun loadData() {
        val id = savedStateHandle[StoreRecycleViewModel.PAGING_ID_KEY] ?: 0
        viewModelScope.launch {
            Log.i("gabe", "paging ix :  $id")
            val response = retroService.getDataFromApi(id)
            // Handle response
            val pageInfo = response.info?.pages
            val namexx = response.results?.get(0)?.name
            Log.i("gabe", "paging ix :  $id || $pageInfo $namexx")
        }
    }

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


//    Note that the factory interface above must be nested inside the ViewModel class otherwise you will get a compilation error.
//    这个工厂模式必需 嵌套在viewModel内，不然有编译错误。

//    Injectable factory per ViewModel
//    Create an interface that each ViewModel factory will need to implement:
    //androidx.hilt.lifecycle.ViewModelAssistedFactory  这个接口协助生成viewModel工厂

    //    Now create a factory per each ViewModel and implement the interface.
//    Make sure to make this factory injectable so that Dagger will inject all the dependencies:
    class StoreRecycleViewModelFactory @Inject constructor(
        private val retroService: RetroService,
    ) : ViewModelAssistedFactory<StoreRecycleViewModel> {
        override fun create(handle: SavedStateHandle): StoreRecycleViewModel {
            return StoreRecycleViewModel(retroService, handle)
        }
    }

//    Since Dagger 2.31, AssistedInject is part of the library.
}