package de.reiss.bible2net.theword2.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import de.reiss.bible2net.theword2.util.extensions.displayDialog

abstract class AppFragment<VB : ViewBinding, VM : ViewModel>(
    @LayoutRes private val fragmentLayout: Int
) : Fragment() {

    var viewModelProvider: ViewModelProvider? = null

    lateinit var viewModel: VM

    private var _binding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    protected val binding get() = _binding!!

    abstract fun defineViewModelProvider(): ViewModelProvider
    abstract fun defineViewModel(): VM

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = defineViewModel()
        initViewModelObservers()
        onAppFragmentReady()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun displayDialog(dialogFragment: DialogFragment) {
        (activity as? AppCompatActivity?)?.displayDialog(dialogFragment)
    }
}
