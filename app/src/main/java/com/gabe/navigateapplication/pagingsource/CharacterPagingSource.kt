package com.gabe.navigateapplication.pagingsource

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gabe.navigateapplication.network.CharacterData
import com.gabe.navigateapplication.network.RetroService
import kotlinx.coroutines.delay

class CharacterPagingSource(private val apiService: RetroService) :
    PagingSource<Int, CharacterData>() {
    override fun getRefreshKey(state: PagingState<Int, CharacterData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterData> {
        return try {
            val nextPage: Int = params.key ?: FIRST_PAGE_INDEX
            val response = apiService.getDataFromApi(nextPage)
            var nextPageNumber: Int? = null
            if (response?.info?.next != null) {
                val uri = Uri.parse(response?.info?.next!!)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
                Log.i("gabe","pagingSource load nextPage[$nextPage]\tnextPageNumber[$nextPageNumber]\turi[${uri.toString()}]" )
            }
            delay(1000L)
            LoadResult.Page(
                data = response?.results!!,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            Log.i("gabe",e.toString())
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }
}