package de.reiss.bible2net.theword.main.content

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.events.DatabaseRefreshed
import de.reiss.bible2net.theword.events.FontSizeChanged
import de.reiss.bible2net.theword.events.TwdDownloadRequested
import de.reiss.bible2net.theword.events.postMessageEvent
import de.reiss.bible2net.theword.formattedDate
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.note.edit.EditNoteActivity
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.preferences.AppPreferencesActivity
import de.reiss.bible2net.theword.util.extensions.*
import de.reiss.bible2net.theword.util.htmlize
import kotlinx.android.synthetic.main.the_word.*
import kotlinx.android.synthetic.main.the_word_content.*
import kotlinx.android.synthetic.main.the_word_empty.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class TheWordFragment : AppFragment<TheWordViewModel>(R.layout.the_word) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = TheWordFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        position = arguments?.getInt(KEY_POSITION, -1) ?: -1
        if (position < 0) {
            throw IllegalStateException("date position unknown")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_share).isEnabled = viewModel.theWord() != null
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_share -> {
                    share()
                    true
                }
                R.id.menu_font_size -> {
                    displayDialog(FontSizePreferenceDialog.createInstance())
                    true
                }
                R.id.menu_date_pick -> {
                    displayDialog(ChooseDayDialog.createInstance(position))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onStart() {
        super.onStart()
        registerToEventBus()
        tryLoad()
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, TheWordViewModel.Factory(
                    App.component.theWordRepository))

    override fun defineViewModel(): TheWordViewModel =
            loadViewModelProvider().get(TheWordViewModel::class.java)

    override fun initViews() {
        the_word_try_download.onClick {
            appPreferences.chosenBible?.let { chosenBible ->
                postMessageEvent(TwdDownloadRequested(
                        bible = chosenBible,
                        year = DaysPositionUtil.dayFor(position)
                ))
            }
        }

        the_word_note_edit.onClick {
            viewModel.theWord()?.let { theWord ->
                activity?.let {
                    it.startActivity(EditNoteActivity.createIntent(
                            context = it,
                            date = theWord.date,
                            theWordContent = theWord.content
                    ))
                }
            }
        }

        the_word_change_translation.onClick {
            activity?.let {
                it.startActivity(AppPreferencesActivity.createIntent(it))
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DatabaseRefreshed) {
        tryLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FontSizeChanged) {
        updateStyle()
    }

    override fun initViewModelObservers() {
        viewModel.theWordLiveData().observe(this, Observer<AsyncLoad<TheWord>> {
            updateUi()
        })
        viewModel.noteLiveData().observe(this, Observer<AsyncLoad<Note>> {
            updateUi()
        })
    }

    private fun updateUi() {
        val context = context ?: return
        val theWord = viewModel.theWord()

        the_word_date.text = formattedDate(context, date().time)

        when {

            viewModel.isLoadingTheWord() -> {
                the_word_loading.loading = true
            }

            (viewModel.isErrorForTheWord() || theWord == null) -> {
                the_word_loading.loading = false
                the_word_empty_root.visibility = VISIBLE
                the_word_content_root.visibility = GONE
            }

            viewModel.isSuccessForTheWord() -> {
                the_word_loading.loading = false
                the_word_empty_root.visibility = GONE
                the_word_content_root.visibility = VISIBLE

                the_word_intro1.textOrHide(htmlize(theWord.content.intro1))
                the_word_text1.text = htmlize(theWord.content.text1)
                the_word_ref1.text = theWord.content.ref1
                the_word_intro2.textOrHide(htmlize(theWord.content.intro2))
                the_word_text2.text = htmlize(theWord.content.text2)
                the_word_ref2.text = theWord.content.ref2
            }
        }

        activity?.invalidateOptionsMenu()

        updateStyle()
    }

    private fun updateStyle() {
        val size = appPreferences.fontSize()
        val contentSize = (size * 1.1).toFloat()
        val refSize = (size * 0.7).toFloat()
        val noteHeaderSize = (size * 0.6).toFloat()
        val noteContentSize = (size * 0.75).toFloat()

        the_word_date.textSize = contentSize
        the_word_intro1.textSize = contentSize
        the_word_text1.textSize = contentSize
        the_word_ref1.textSize = refSize
        the_word_intro2.textSize = contentSize
        the_word_text2.textSize = contentSize
        the_word_ref2.textSize = refSize
        the_word_note_header.textSize = noteHeaderSize
        the_word_note_content.textSize = refSize
        the_word_note_edit.textSize = noteContentSize

        the_word_note.visibleElseGone(appPreferences.showNotes())
        if (viewModel.isLoadingNote().not()) {
            the_word_note_content.text = viewModel.note()?.noteText ?: ""
        }

    }

    private fun tryLoad() {
        val date = date()

        if (viewModel.isLoadingTheWord().not()) {
            viewModel.loadTheWord(date)
        }
        if (viewModel.isLoadingNote().not()) {
            viewModel.loadNote(date)
        }
    }

    private fun date() = DaysPositionUtil.dayFor(position).time

    private fun share() {
        context?.let { context ->
            viewModel.theWord()?.let { theWord ->
                displayDialog(ShareDialog.createInstance(
                        context = context,
                        time = theWord.date.time,
                        theWordContent = theWord.content,
                        note = viewModel.note()?.noteText ?: ""
                ))
            }
        }
    }

}