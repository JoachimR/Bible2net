package de.reiss.bible2net.theword.architecture

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.VisibleForTesting
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.reiss.bible2net.theword.util.extensions.displayDialog


abstract class AppFragment<T : ViewModel>(@LayoutRes private val fragmentLayout: Int) : Fragment() {

    var viewModelProvider: ViewModelProvider? = null

    lateinit var viewModel: T

    abstract fun defineViewModelProvider(): ViewModelProvider
    abstract fun defineViewModel(): T

    abstract fun initViews()
    abstract fun initViewModelObservers()

    open fun onAppFragmentReady() {
    }

    @VisibleForTesting
    fun initViewModelProvider(viewModelProvider: ViewModelProvider) {
        this.viewModelProvider = viewModelProvider
    }

    fun loadViewModelProvider(): ViewModelProvider {
        if (viewModelProvider == null) {
            viewModelProvider = defineViewModelProvider()
        }
        return viewModelProvider!!
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(fragmentLayout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = defineViewModel()
        initViewModelObservers()
        onAppFragmentReady()
    }

    protected fun displayDialog(dialogFragment: DialogFragment) {
        (activity as? AppCompatActivity?)?.displayDialog(dialogFragment)
    }

}
