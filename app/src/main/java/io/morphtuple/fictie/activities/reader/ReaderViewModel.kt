package io.morphtuple.fictie.activities.reader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.morphtuple.fictie.models.FicUserStuff
import io.morphtuple.fictie.services.AO3Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(private val ao3Service: AO3Service) : ViewModel() {
    val fic by lazy {
        MutableLiveData<FicUserStuff?>(null)
    }

    val networkError by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun getFic(ficId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fic.postValue(ao3Service.getFic(ficId))
            } catch (e: Exception) {
                networkError.postValue(true)
            }
        }
    }
}