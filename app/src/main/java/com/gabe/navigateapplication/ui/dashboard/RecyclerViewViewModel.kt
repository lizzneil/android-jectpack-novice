package com.gabe.navigateapplication.ui.dashboard

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
import kotlinx.coroutines.flow.Flow

class RecyclerViewViewModel( private val retroService: RetroService) : ViewModel() {
//    var retroService: RetroService = RetroInstance.getRetroInstance().create(RetroService::class.java)


    fun getListData(): Flow<PagingData<CharacterData>> {
        return Pager(
            //这里不要限制最大数，当记录超最大数时，超的会空指针。除非你有把制握不超。
            config = PagingConfig(pageSize = 20 ),//PagingConfig(pageSize = 34, maxSize = 200),
            pagingSourceFactory = { CharacterPagingSource(retroService) }
        ).flow.cachedIn(viewModelScope)
    }
}