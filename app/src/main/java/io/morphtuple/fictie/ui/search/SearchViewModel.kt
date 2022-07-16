package io.morphtuple.fictie.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.services.AO3SearchPagingSource
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ao3Service: AO3Service,
) : ViewModel() {
    val anyField = MutableStateFlow("")
    val netActivity: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    private var currentSource: AO3SearchPagingSource? = null

    val searchFlow = anyField.flatMapLatest { q ->
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            // TODO Are you inviting memory leaks?
            // This feels like an overengineered way to get network activity
            // Perhaps just observe the main Retrofit instance for calls

            currentSource = AO3SearchPagingSource(ao3Service, q)
            currentSource!!.isLoading.observeForever {
                netActivity.postValue(it)
            }

            currentSource!!
        }.flow.cachedIn(viewModelScope)
    }

    fun toggleBookmark(partialFic: PartialFic) {
        viewModelScope.launch(Dispatchers.IO) {
            ao3Service.toggleBookmark(partialFic)
        }
    }
}