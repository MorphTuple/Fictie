package io.morphtuple.fictie.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.PartialFic
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val ao3Service: AO3Service
) : ViewModel() {
    fun getLibraryLiveData(): LiveData<List<PartialFic>> {
        return ao3Service.getLibraryLiveData()
    }

    fun toggleBookmark(partialFic: PartialFic) {
        viewModelScope.launch(Dispatchers.IO) {
            ao3Service.toggleBookmark(partialFic)
        }
    }
}