package io.morphtuple.fictie.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.FicSearchQuery
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val ao3Service: AO3Service) : ViewModel() {
    val liveSearchResult by lazy {
        MutableLiveData<List<PartialFic>?>(null)
    }

    fun search(anyField: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ao3Service.search(FicSearchQuery(anyField))
            liveSearchResult.postValue(result)
        }
    }
}