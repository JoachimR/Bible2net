package de.reiss.bible2net.theword.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword.util.extensions.firstDayOfYear
import de.reiss.bible2net.theword.util.extensions.lastDayOfYear
import java.util.*

open class ViewPagerViewModel(initialBible: String,
                              private val repository: ViewPagerRepository) : ViewModel() {

    private val loadYearLiveData: MutableLiveData<AsyncLoad<String>> = MutableLiveData()

    init {
        loadYearLiveData.postValue(AsyncLoad.loading(initialBible))
    }

    open fun loadYearLiveData() = loadYearLiveData

    open fun prepareContentFor(bible: String, date: Date) {
        repository.getItemsFor(
                bible = bible,
                fromDate = date.firstDayOfYear(),
                toDate = date.lastDayOfYear(),
                result = loadYearLiveData()
        )
    }

    fun currentBible() = loadYearLiveData().value?.data

    fun isLoadingContent() = loadYearLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val initialBible: String,
                  private val repository: ViewPagerRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return ViewPagerViewModel(initialBible, repository) as T
        }

    }

}