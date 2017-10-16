package de.reiss.bible2net.theword.bible


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.bible.list.BibleListBuilder
import de.reiss.bible2net.theword.bible.list.BibleListItemAdapter
import de.reiss.bible2net.theword.model.Bible
import de.reiss.bible2net.theword.util.extensions.onClick
import kotlinx.android.synthetic.main.bible_fragment.*


class BibleFragment : AppFragment<BibleViewModel>(R.layout.bible_fragment), BibleClickListener {

    companion object {

        fun createInstance() = BibleFragment()

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private lateinit var bibleListItemAdapter: BibleListItemAdapter

    override fun initViews() {
        bibleListItemAdapter = BibleListItemAdapter(bibleClickListener = this)

        with(bible_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = bibleListItemAdapter
        }

        bible_no_bibles_refresh.onClick {
            tryRefresh()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, BibleViewModel.Factory(
                    App.component.bibleRepository))

    override fun defineViewModel(): BibleViewModel =
            loadViewModelProvider().get(BibleViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.biblesLiveData.observe(this, Observer<AsyncLoad<List<Bible>>> {
            onResourceChange()
        })
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
        bible_loading_bibles.visibility = GONE
        bible_no_bibles.visibility = GONE
        bible_recycler_view.visibility = GONE

        if (viewModel.isLoadingBibles()) {
            bible_loading_bibles.visibility = VISIBLE
        } else {
            BibleListBuilder.buildList(viewModel.bibles()).let { listItems ->
                if (listItems.isEmpty()) {
                    bible_no_bibles.visibility = VISIBLE
                } else {
                    bible_recycler_view.visibility = VISIBLE
                    bibleListItemAdapter.updateContent(listItems)
                }
            }
        }
    }

}