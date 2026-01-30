package de.reiss.bible2net.theword2.architecture

import de.reiss.bible2net.theword2.architecture.AsyncLoadStatus.ERROR
import de.reiss.bible2net.theword2.architecture.AsyncLoadStatus.LOADING
import de.reiss.bible2net.theword2.architecture.AsyncLoadStatus.SUCCESS

data class AsyncLoad<out T>(
    val loadStatus: AsyncLoadStatus,
    val data: T?,
    val message: String?
) {

    companion object {

        fun <T> success(newData: T? = null, message: String? = null): AsyncLoad<T> =
            AsyncLoad(SUCCESS, newData, message)

        fun <T> error(oldData: T? = null, message: String? = null): AsyncLoad<T> =
            AsyncLoad(ERROR, oldData, message)

        fun <T> loading(oldData: T? = null, message: String? = null): AsyncLoad<T> =
            AsyncLoad(LOADING, oldData, message)
    }
}
