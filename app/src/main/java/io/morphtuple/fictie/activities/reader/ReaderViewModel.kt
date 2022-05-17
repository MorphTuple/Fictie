package io.morphtuple.fictie.activities.reader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.Fic
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val ao3Service: AO3Service) : ViewModel() {
    val fic by lazy {
        MutableLiveData<Fic?>(null)
    }

    fun getFic(ficId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fic.postValue(ao3Service.getFic(ficId))
        }
    }
}