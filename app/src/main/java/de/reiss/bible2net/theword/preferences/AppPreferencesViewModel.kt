package de.reiss.bible2net.theword.preferences

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword.model.Bible

class AppPreferencesViewModel(private val repository: AppPreferencesRepository) : ViewModel() {

    var biblesLiveData: MutableLiveData<AsyncLoad<List<Bible>>> = MutableLiveData()

    fun bibles() = biblesLiveData.value?.data ?: emptyList()

    fun loadBibles() {
        repository.loadBibleItems(biblesLiveData)
    }

    fun isLoadingBibles() = biblesLiveData.value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: AppPreferencesRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return AppPreferencesViewModel(repository) as T
        }
    }
}
