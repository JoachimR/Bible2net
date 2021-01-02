package de.reiss.bible2net.theword.main.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.databinding.TheWordFragmentBinding
import de.reiss.bible2net.theword.events.DatabaseRefreshed
import de.reiss.bible2net.theword.events.FontSizeChanged
import de.reiss.bible2net.theword.events.TwdDownloadRequested
import de.reiss.bible2net.theword.events.postMessageEvent
import de.reiss.bible2net.theword.formattedDate
import de.reiss.bible2net.theword.note.edit.EditNoteActivity
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.preferences.AppPreferencesActivity
import de.reiss.bible2net.theword.util.copyToClipboard
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.extensions.registerToEventBus
import de.reiss.bible2net.theword.util.extensions.showShortSnackbar
import de.reiss.bible2net.theword.util.extensions.textOrHide
import de.reiss.bible2net.theword.util.extensions.unregisterFromEventBus
import de.reiss.bible2net.theword.util.extensions.visibleElseGone
import de.reiss.bible2net.theword.util.htmlize
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TheWordFragment : AppFragment<TheWordFragmentBinding, TheWordViewModel>(
    R.layout.the_word_fragment
) {

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
        ViewModelProviders.of(
            this,
            TheWordViewModel.Factory(
                App.component.theWordRepository
            )
        )

    override fun defineViewModel(): TheWordViewModel =
        loadViewModelProvider().get(TheWordViewModel::class.java)

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TheWordFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        with(binding.emptyRoot) {
            tryDownload.onClick {
                appPreferences.chosenBible?.let { chosenBible ->
                    postMessageEvent(
                        TwdDownloadRequested(
                            bible = chosenBible,
                            year = DaysPositionUtil.dayFor(position)
                        )
                    )
                }
            }
            changeTranslation.onClick {
                activity?.let {
                    it.startActivity(AppPreferencesActivity.createIntent(it))
                }
            }
        }

        with(binding.contentRoot) {
            noteEdit.onClick {
                viewModel.theWord()?.let { theWord ->
                    activity?.let {
                        it.startActivity(
                            EditNoteActivity.createIntent(
                                context = it,
                                date = theWord.date,
                                theWordContent = theWord.content
                            )
                        )
                    }
                }
            }
            ref1.setOnLongClickListener {
                context?.let {
                    copyToClipboard(it, ref1.text.toString())
                    showShortSnackbar(message = R.string.copied_to_clipboard)
                }
                true
            }
            ref2.setOnLongClickListener {
                context?.let {
                    copyToClipboard(it, ref2.text.toString())
                    showShortSnackbar(message = R.string.copied_to_clipboard)
                }
                true
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
        viewModel.theWordLiveData().observe(this, { updateUi() })
        viewModel.noteLiveData().observe(this, { updateUi() })
    }

    private fun updateUi() {
        val context = context ?: return
        val theWord = viewModel.theWord()

        binding.date.text = formattedDate(context, date().time)

        when {
            viewModel.isLoadingTheWord() -> {
                binding.loading.setLoading(true)
            }

            (viewModel.isErrorForTheWord() || theWord == null) -> {
                binding.loading.setLoading(false)
                binding.emptyRoot.root.visibility = VISIBLE
                binding.contentRoot.root.visibility = GONE
            }

            viewModel.isSuccessForTheWord() -> {
                binding.loading.setLoading(false)
                binding.emptyRoot.root.visibility = GONE

                with(binding.contentRoot) {
                    root.visibility = VISIBLE

                    intro1.textOrHide(htmlize(theWord.content.intro1))
                    text1.text = htmlize(theWord.content.text1)
                    ref1.text = theWord.content.ref1
                    intro2.textOrHide(htmlize(theWord.content.intro2))
                    text2.text = htmlize(theWord.content.text2)
                    ref2.text = theWord.content.ref2
                }
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

        binding.date.textSize = contentSize
        with(binding.contentRoot) {
            intro1.textSize = contentSize
            text1.textSize = contentSize
            ref1.textSize = refSize
            intro2.textSize = contentSize
            text2.textSize = contentSize
            ref2.textSize = refSize
            noteHeader.textSize = noteHeaderSize
            noteContent.textSize = noteContentSize
            noteEdit.textSize = noteContentSize

            note.visibleElseGone(appPreferences.showNotes())

            if (viewModel.isLoadingNote().not()) {
                noteContent.text = viewModel.note()?.noteText ?: ""
            }
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
                displayDialog(
                    ShareDialog.createInstance(
                        context = context,
                        time = theWord.date.time,
                        theWordContent = theWord.content,
                        note = viewModel.note()?.noteText ?: ""
                    )
                )
            }
        }
    }
}
