package io.morphtuple.fictie.services

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.morphtuple.fictie.models.FicSearchQuery
import io.morphtuple.fictie.models.PartialFic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AO3SearchPagingSource(
    private val aO3Service: AO3Service,
    private val searchQuery: FicSearchQuery
) : PagingSource<Int, PartialFic>() {
    override fun getRefreshKey(state: PagingState<Int, PartialFic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PartialFic> {
        return try {
            val pageIndex = params.key ?: 1

            withContext(Dispatchers.IO) {
                val response = aO3Service.search(searchQuery, pageIndex)
                val nextPageNumber = if (response.isEmpty()) null else {
                    pageIndex + (params.loadSize / 20)
                }

                LoadResult.Page(
                    data = response,
                    prevKey = if (pageIndex == 1) null else pageIndex,
                    nextKey = nextPageNumber
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}