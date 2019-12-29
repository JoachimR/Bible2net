package de.reiss.bible2net.theword.main.viewpager

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.testutil.FragmentTest
import de.reiss.bible2net.theword.testutil.assertDisplayed
import de.reiss.bible2net.theword.testutil.assertNotDisplayed
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewPagerFragmentTest : FragmentTest<ViewPagerFragment>() {

    private val chosenBible = "someChosenBible"

    private val loadYearLiveData = MutableLiveData<AsyncLoad<String>>()

    private val mockedViewModel = mock<ViewPagerViewModel> {
        on { loadYearLiveData() } doReturn loadYearLiveData
    }

    override fun createFragment(): ViewPagerFragment =
            ViewPagerFragment.createInstance()
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<ViewPagerViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        loadYearLiveData.postValue(AsyncLoad.success(chosenBible))
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        loadYearLiveData.postValue(AsyncLoad.loading(chosenBible))

        assertDisplayed(R.id.view_pager_loading)
        assertNotDisplayed(R.id.view_pager)
    }

    @Test
    fun whenDoneLoadingThenShowViewPager() {
        loadYearLiveData.postValue(AsyncLoad.success(chosenBible))

        assertDisplayed(R.id.view_pager)
        assertNotDisplayed(R.id.view_pager_loading)
    }

}
