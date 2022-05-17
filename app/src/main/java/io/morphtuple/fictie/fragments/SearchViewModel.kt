package io.morphtuple.fictie.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.FicSearchQuery
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.services.AO3SearchPagingSource
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val ao3Service: AO3Service) : ViewModel() {
    val anyField = MutableStateFlow("")

    val searchFlow = anyField.flatMapLatest{ q ->
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            AO3SearchPagingSource(ao3Service, FicSearchQuery(q))
        }.flow.cachedIn(viewModelScope)
    }
}