package de.reiss.bible2net.theword2.bible

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.reiss.bible2net.theword2.App
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.architecture.AppFragment
import de.reiss.bible2net.theword2.bible.list.BibleListItem
import de.reiss.bible2net.theword2.bible.list.BibleListItemAdapter
import de.reiss.bible2net.theword2.databinding.BibleFragmentBinding
import de.reiss.bible2net.theword2.model.Bible
import de.reiss.bible2net.theword2.util.extensions.onClick
import de.reiss.bible2net.theword2.util.sortBibles

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
        ViewModelProvider(
            this,
            BibleViewModel.Factory(
                App.component.bibleRepository
            )
        )

    override fun defineViewModel(): BibleViewModel =
        loadViewModelProvider()[BibleViewModel::class.java]

    override fun initViewModelObservers() {
        viewModel.biblesLiveData.observe(this) { onResourceChange() }
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
            sortBibles(viewModel.bibles()).map { BibleListItem(it) }.let { listItems ->
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
