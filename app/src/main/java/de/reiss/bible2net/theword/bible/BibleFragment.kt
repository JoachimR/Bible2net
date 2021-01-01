package de.reiss.bible2net.theword.bible

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.bible.list.BibleListBuilder
import de.reiss.bible2net.theword.bible.list.BibleListItemAdapter
import de.reiss.bible2net.theword.databinding.BibleFragmentBinding
import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.extensions.onClick

class BibleFragment :
    AppFragment<BibleFragmentBinding, BibleViewModel>(R.layout.bible_fragment),
    BibleClickListener {

    companion object {
        fun createInstance() = BibleFragment()
    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private lateinit var bibleListItemAdapter: BibleListItemAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        BibleFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        bibleListItemAdapter = BibleListItemAdapter(bibleClickListener = this)

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = bibleListItemAdapter
        }

        binding.noBiblesRefresh.onClick {
            tryRefresh()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this,
            BibleViewModel.Factory(
                App.component.bibleRepository
            )
        )

    override fun defineViewModel(): BibleViewModel =
        loadViewModelProvider().get(BibleViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.biblesLiveData.observe(this, { onResourceChange() })
    }

    override fun onAppFragmentReady() {
        tryRefresh()
    }

    override fun onBibleClicked(bible: Bible) {
        appPreferences.chosenBible = bible.key
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingBibles().not()) {
            viewModel.refreshBibles()
        }
    }

    private fun onResourceChange() {
        binding.loading.visibility = GONE
        binding.noBibles.visibility = GONE
        binding.recyclerView.visibility = GONE

        if (viewModel.isLoadingBibles()) {
            binding.loading.visibility = VISIBLE
        } else {
            BibleListBuilder.buildList(viewModel.bibles()).let { listItems ->
                if (listItems.isEmpty()) {
                    binding.noBibles.visibility = VISIBLE
                } else {
                    binding.recyclerView.visibility = VISIBLE
                    bibleListItemAdapter.updateContent(listItems)
                }
            }
        }
    }
}
