package io.morphtuple.fictie.ui.reader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.FicNavigation
import io.morphtuple.fictie.models.FicPage
import io.morphtuple.fictie.services.AO3FicLoaderPagingSource
import io.morphtuple.fictie.services.AO3SearchPagingSource
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val ao3Service: AO3Service) : ViewModel() {
    val networkError by lazy {
        MutableLiveData(false)
    }

    private val navigation = MutableStateFlow<FicNavigation?>(null)

    val flow = navigation.filterNotNull()
        .flatMapLatest {
            Pager(PagingConfig(pageSize = 1000, enablePlaceholders = false)) {
                AO3FicLoaderPagingSource(ao3Service, navigation.value!!)
            }.flow.cachedIn(viewModelScope)
        }

    fun getFic(ficId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                navigation.value = ao3Service.getNavigation(ficId)
            } catch (e: Exception) {
                networkError.postValue(true)
            }
        }
    }
}