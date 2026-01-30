package de.reiss.bible2net.theword2.bible

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword2.architecture.AsyncLoad
import de.reiss.bible2net.theword2.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword2.model.Bible

class BibleViewModel(private val repository: BibleRepository) : ViewModel() {

    var biblesLiveData: MutableLiveData<AsyncLoad<List<Bible>>> = MutableLiveData()

    fun bibles() = biblesLiveData.value?.data ?: emptyList()

    fun refreshBibles() {
        repository.updateBibles(biblesLiveData)
    }

    fun isLoadingBibles() = biblesLiveData.value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: BibleRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return BibleViewModel(repository) as T
        }
    }
}
