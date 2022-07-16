package io.morphtuple.fictie.services

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.morphtuple.fictie.models.FicElement
import io.morphtuple.fictie.models.FicNavigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AO3FicLoaderPagingSource(
    private val aO3Service: AO3Service,
    private val navigation: FicNavigation
) : PagingSource<Int, FicElement>() {
    override fun getRefreshKey(state: PagingState<Int, FicElement>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FicElement> {
        return try {
            val pageIndex = (params.key ?: 1)

            withContext(Dispatchers.IO) {
                val res = aO3Service.getChapterElements(
                    navigation.ficId,
                    navigation.chapters[pageIndex - 1].chapterId
                )

                LoadResult.Page(
                    data = res,
                    prevKey = if (pageIndex == 1) null else pageIndex,
                    nextKey = if (navigation.chapters.size == pageIndex) null else pageIndex + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}