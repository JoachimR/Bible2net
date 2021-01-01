package de.reiss.bible2net.theword.main.viewpager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.databinding.ViewPagerFragmentBinding
import de.reiss.bible2net.theword.events.DatabaseRefreshed
import de.reiss.bible2net.theword.events.TwdDownloadRequested
import de.reiss.bible2net.theword.events.ViewPagerMoveRequest
import de.reiss.bible2net.theword.events.postMessageEvent
import de.reiss.bible2net.theword.util.extensions.registerToEventBus
import de.reiss.bible2net.theword.util.extensions.unregisterFromEventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class ViewPagerFragment : AppFragment<ViewPagerFragmentBinding, ViewPagerViewModel>(R.layout.view_pager_fragment) {

    companion object {

        private val KEY_INITIAL_POS = "KEY_INITIAL_POS"
        private val KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION"

        private val INVALID_POSITION = -1

        fun createInstance(position: Int? = null) = ViewPagerFragment().apply {
            arguments = Bundle().apply {
                if (position != null) {
                    putInt(KEY_INITIAL_POS, position)
                }
            }
        }
    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private var savedPosition = INVALID_POSITION

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPosition(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        registerToEventBus()

        if (appPreferences.chosenBible != viewModel.currentBible()) {
            tryRefresh()
        }
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    private fun loadPosition(savedInstanceState: Bundle?) {
        val initialPos = arguments?.getInt(KEY_INITIAL_POS, INVALID_POSITION) ?: -1
        arguments?.remove(KEY_INITIAL_POS)
        savedPosition = when {
            initialPos != INVALID_POSITION -> initialPos
            else -> savedInstanceState?.getInt(KEY_CURRENT_POSITION)
                    ?: DaysPositionUtil.positionFor(Calendar.getInstance())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_CURRENT_POSITION, currentPosition())
        super.onSaveInstanceState(outState)
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
            ViewPagerFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        adapter = ViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            appPreferences.chosenBible.let { chosenBible ->
                if (chosenBible == null) {
                    throw IllegalStateException("No bible chosen")
                }
                return ViewModelProviders.of(this,
                        ViewPagerViewModel.Factory(
                                initialBible = chosenBible,
                                repository = App.component.viewPagerRepository))
            }

    override fun defineViewModel(): ViewPagerViewModel =
            loadViewModelProvider().get(ViewPagerViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.loadYearLiveData().observe(this, Observer<AsyncLoad<String>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        goToPosition(savedPosition)
        updateUi()
        tryRefresh()
    }

    private fun updateUi() {
        if (viewModel.isLoadingContent()) {
            viewModel.currentBible().let {
                if (it == null) {
                    throw IllegalStateException("Loading unknown content")
                } else {
                    binding.viewPagerLoadingText.text =
                            getString(R.string.view_pager_loading_content, it)
                }
            }
            binding.viewPagerLoading.visibility = VISIBLE
            binding.viewPager.visibility = GONE
        } else {
            binding.viewPagerLoading.visibility = GONE
            binding.viewPager.visibility = VISIBLE
            postMessageEvent(DatabaseRefreshed())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: TwdDownloadRequested) {
        if (viewModel.isLoadingContent().not()) {
            appPreferences.chosenBible?.let { chosenBible ->
                viewModel.prepareContentFor(
                        bible = chosenBible,
                        date = event.year.time)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ViewPagerMoveRequest) {
        goToPosition(event.position)
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingContent().not()) {
            appPreferences.chosenBible?.let { chosenBible ->
                viewModel.prepareContentFor(
                        bible = chosenBible,
                        date = DaysPositionUtil.dayFor(savedPosition).time)
            }
        }
    }

    private fun goToPosition(positionInFocus: Int) {
        binding.viewPager.currentItem = positionInFocus
    }

    private fun currentPosition() = binding.viewPager.currentItem
}
