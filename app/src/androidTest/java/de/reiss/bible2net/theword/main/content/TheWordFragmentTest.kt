package de.reiss.bible2net.theword.main.content

import android.arch.lifecycle.MutableLiveData
import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.testutil.*
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TheWordFragmentTest : FragmentTest<TheWordFragment>() {

    private val theWordLiveData = MutableLiveData<AsyncLoad<TheWord>>()
    private val noteLiveData = MutableLiveData<AsyncLoad<Note>>()

    private val mockedViewModel = mock<TheWordViewModel> {
        on { theWordLiveData() } doReturn theWordLiveData
        on { noteLiveData() } doReturn noteLiveData
    }

    override fun createFragment(): TheWordFragment =
            TheWordFragment.createInstance(
                    DaysPositionUtil.positionFor(timeForTest()))
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<TheWordViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        theWordLiveData.postValue(AsyncLoad.loading())

        assertDisplayed(R.id.the_word_loading)
        assertNotDisplayed(R.id.the_word_empty_root, R.id.the_word_content_root)
    }

    @Test
    fun whenEmptyThenShowEmpty() {
        theWordLiveData.postValue(AsyncLoad.success(null))

        assertDisplayed(R.id.the_word_empty_root)
        assertNotDisplayed(R.id.the_word_loading, R.id.the_word_content_root)
    }

    @Test
    fun whenContentThenShowContent() {
        val theWord = sampleTheWord(0, "testBible")
        theWordLiveData.postValue(AsyncLoad.success(theWord))

        assertDisplayed(R.id.the_word_content_root)
        assertNotDisplayed(R.id.the_word_loading, R.id.the_word_empty_root)
        checkIsTextSet {
            R.id.the_word_intro1 to theWord.content.intro1
        }
        checkIsTextSet {
            R.id.the_word_text1 to theWord.content.text1
        }
        checkIsTextSet {
            R.id.the_word_ref1 to theWord.content.ref1
        }
        checkIsTextSet {
            R.id.the_word_intro2 to theWord.content.intro2
        }
        checkIsTextSet {
            R.id.the_word_text2 to theWord.content.text2
        }
        checkIsTextSet {
            R.id.the_word_ref2 to theWord.content.ref2
        }
    }

    private fun timeForTest() = Calendar.getInstance().apply {
        time = time.withZeroDayTime()
    }

}