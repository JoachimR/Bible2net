@file:Suppress("unused")

package de.reiss.bible2net.theword.testutil

import android.app.Activity
import android.app.Instrumentation
import android.content.res.Resources
import android.os.Build
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.internal.NavigationMenuView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.security.SecureRandom

@Suppress("unused")

fun checkIsTextSet(arg: () -> Pair<Int, String>) {
    val pair = arg()
    onView(withId(pair.first)).check(matches(withText(pair.second)))
}

fun clickOnFirstView(@IdRes viewResId: Int) {
    onView(firstMatch(withId(viewResId))).perform(click())
}

fun clickOnFirstView(text: String) {
    onView(firstMatch(withText(text))).perform(click())
}

fun clickOnPositiveAlertDialogButton() {
    clickOnButtonInAlertDialog(AlertDialogButton.POSITIVE)
}

fun clickOnNegativeAlertDialogButton() {
    clickOnButtonInAlertDialog(AlertDialogButton.NEGATIVE)
}

fun assertTextInSnackbar(string: String) {
    onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(string)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
}

fun assertTextInSnackbar(@StringRes stringResId: Int) {
    onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(stringResId)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
}

fun assertSnackbarIsDisplayed() {
    onSnackbar().check(matches(isDisplayed()))
}

private fun onSnackbar(): ViewInteraction =
        onView(withId(com.google.android.material.R.id.snackbar_text))

fun overflowButtonMatcher(): Matcher<View> {
    return anyOf(
            withContentDescription("More options"),
            withClassName(endsWith("OverflowMenuButton")))
}

fun pickTime(hours: Int, minutes: Int) {
    onView(withClassName(`is`(TimePicker::class.java.name)))
            .inRoot(isDialog())
            .perform(PickerActions.setTime(hours, minutes))

    onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .perform(click())
}

fun pickDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
    onView(withClassName(`is`(DatePicker::class.java.name)))
            .inRoot(isDialog())
            .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth))

    onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .perform(click())
}

fun swipeLeftInViewPager(@IdRes viewPagerResId: Int) {
    onView(withId(viewPagerResId)).perform(swipeLeft());
}

fun swipeRightInViewPager(@IdRes viewPagerResId: Int) {
    onView(withId(viewPagerResId)).perform(swipeRight());
}

private fun androidHomeMatcher(): Matcher<View> {
    return allOf(
            withParent(withClassName(`is`(Toolbar::class.java.name))),
            withClassName(anyOf(
                    `is`(ImageButton::class.java.name),
                    `is`(AppCompatImageButton::class.java.name)
            )))
}

fun mockActivityIntent(clazz: Class<*>) {
    intending(hasComponent(clazz.name))
            .respondWith(nullActivityResult())
}

private fun nullActivityResult(): Instrumentation.ActivityResult =
        Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null)

fun assertIntendedActivity(clazz: Class<*>) {
    intended(hasComponent(clazz.name))
}

fun assertActivityResultOK(activity: Activity) {
    val field = Activity::class.java.getDeclaredField("mResultCode")
    field.isAccessible = true
    val resultCode = field.getInt(activity)
    assertTrue("The activity result code is not Activity.RESULT_OK.",
            resultCode == Activity.RESULT_OK)
}

fun scrollToAndClickNavigationDrawerItem(viewMatcher: Matcher<View>) {
    onView(withClassName(`is`(NavigationMenuView::class.java.name)))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(viewMatcher)))
    onView(viewMatcher).perform(click())
}

fun checkTextsAreDisplayed(@StringRes vararg stringResIds: Int) {
    for (resId in stringResIds) {
        onView(firstMatch(withText(resId))).check(matches(isDisplayed()))
    }
}

fun checkTextsAreDisplayed(vararg strings: String) {
    for (s in strings) {
        onView(firstMatch(withText(s))).check(matches(isDisplayed()))
    }
}

fun checkTextsDoNotExist(@StringRes vararg stringResIds: Int) {
    for (resId in stringResIds) {
        onView(withText(resId)).check(doesNotExist())
    }
}

fun assertDisplayed(@IdRes vararg viewResIds: Int) {
    for (resId in viewResIds) {
        onView(firstMatch(withId(resId))).check(matches(isDisplayed()))
    }
}

fun assertNotDisplayed(@IdRes vararg viewResIds: Int) {
    for (resId in viewResIds) {
        onView(firstMatch(withId(resId))).check(matches(not(isDisplayed())))
    }
}

fun checkIfToastMessageDisplayed(@StringRes resId: Int) {
    onView(withText(resId)).inRoot(toastMatcher()).check(matches(isDisplayed()))
}

private fun toastMatcher(): TypeSafeMatcher<Root> {
    return object : TypeSafeMatcher<Root>() {

        override fun describeTo(description: Description) {
            description.appendText("is toast")
        }

        public override fun matchesSafely(root: Root): Boolean {
            val type = root.windowLayoutParams.get().type
            if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
                val windowToken = root.decorView.windowToken
                val appToken = root.decorView.applicationWindowToken
                if (windowToken === appToken) {
                    // windowToken == appToken means this window isn't contained by any other windows.
                    // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                    return true
                }
            }
            return false
        }
    }
}

fun setNumberPicker(value: Int): ViewAction {

    return object : ViewAction {

        override fun perform(uiController: UiController, view: View) {
            (view as NumberPicker).value = value
        }

        override fun getDescription(): String =
                "Set the number picker value"

        override fun getConstraints(): Matcher<View> =
                isAssignableFrom(NumberPicker::class.java)

    }
}

@Throws(InterruptedException::class)
fun assertActivityIsFinished(activityRule: ActivityTestRule<*>) {
    waitForIdleSync()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        assertTrue(activityRule.activity.isFinishing)
    }
}

private fun waitForIdleSync() {
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
}

fun assertEnabled(@IdRes vararg viewResIds: Int) {
    for (viewResId in viewResIds) {
        onView(withId(viewResId)).check(matches(isEnabled()))
    }
}

fun assertDisabled(@IdRes vararg viewResIds: Int) {
    for (viewResId in viewResIds) {
        onView(withId(viewResId)).check(matches(not(isEnabled())))
    }
}

fun clickOnItemOnMenuPopupWindow(activity: Activity, text: String) {
    onView(withText(text))
            .inRoot(RootMatchers.withDecorView(not(`is`(activity.window.decorView))))
            .perform(click())
}

fun selectItemOnSpinner(@IdRes spinner: Int, @StringRes text: Int) {
    selectItemOnSpinner(spinner, InstrumentationRegistry.getInstrumentation().targetContext.getString(text))
}

private fun selectItemOnSpinner(@IdRes spinner: Int, text: String) {
    onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`(text)))
            .inAdapterView(withId(spinner))
            .perform(click())
    Espresso.pressBack()
}

fun clickHomeIcon() {
    onView(androidHomeMatcher()).perform(click())
}

fun runOnUiThreadAndIdleSync(rule: ActivityTestRule<*>, runnable: Runnable) {
    try {
        rule.runOnUiThread(runnable)
        waitForIdleSync()
    } catch (throwable: Throwable) {
        throw RuntimeException(throwable)
    }

}

fun onRecyclerView(@IdRes recyclerViewResId: Int,
                   itemPosition: Int,
                   @IdRes viewInItem: Int): ViewInteraction {
    onView(withId(recyclerViewResId)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemPosition))

    return onView(RecyclerViewMatcher.withRecyclerView(recyclerViewResId)
            .atPositionOnView(itemPosition, viewInItem))
}

fun clickOnRecyclerView(@IdRes recyclerViewResId: Int,
                        itemPosition: Int,
                        @IdRes viewInItem: Int) {
    onRecyclerView(recyclerViewResId, itemPosition, viewInItem)
            .perform(click())
}

fun assertRecyclerViewEmpty(@IdRes recyclerViewResId: Int) {
    onView(withId(recyclerViewResId)).check(hasItemsCount(0))
}

fun assertRecyclerViewItemsCount(@IdRes recyclerViewResId: Int, count: Int) {
    onView(withId(recyclerViewResId)).check(hasItemsCount(count))
}

private fun hasItemsCount(count: Int): ViewAssertion {
    return ViewAssertion { view, e ->
        if (view !is RecyclerView) {
            throw e
        }

        assertEquals(count.toLong(), view.adapter!!.itemCount.toLong())
    }
}

fun setEditText(arg: () -> Pair<Int, String>) {
    val pair = arg()
    onView(withId(pair.first))
            .perform(clearText())
            .perform(typeText(pair.second))
    Espresso.closeSoftKeyboard()
}

fun assertTextInputLayoutErrorShown(@LayoutRes view: Int, @StringRes text: Int) {
    onErrorFromTextInputLayout(view).check(matches(withText(text)))
}

private fun onErrorFromTextInputLayout(@IdRes textInputLayoutId: Int): ViewInteraction {
    return onView(allOf(isDescendantOfA(withId(textInputLayoutId)),
            not(isAssignableFrom(EditText::class.java)),
            isAssignableFrom(TextView::class.java)))
}

fun getCurrentFragmentActivity(): FragmentActivity {
    var currentActivity: FragmentActivity? = null
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            val next = resumedActivities.iterator().next()
            if (next is FragmentActivity) {
                currentActivity = next
            }
        }
    }
    return currentActivity!!
}

private val abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
private var random = SecureRandom()

fun randomText(length: Int): String {
    return StringBuilder(length).apply {
        (0 until length).forEach {
            append(abc[random.nextInt(abc.length)])
        }
    }.toString()
}

private fun <T> firstMatch(matcher: Matcher<T>): Matcher<T> {

    return object : BaseMatcher<T>() {

        internal var isFirst = true

        override fun matches(item: Any): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }

            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("should return first matching item")
            description.appendText(matcher.toString())
        }
    }

}

fun assertAlphaValue(@IdRes viewResId: Int, alpha: Float) {
    onView(withId(viewResId)).check(matches(withAlpha(alpha)))
}

private fun withAlpha(alpha: Float): Matcher<View> {

    return object : BoundedMatcher<View, View>(View::class.java) {

        override fun matchesSafely(item: View): Boolean = item.alpha == alpha

        override fun describeTo(description: Description) {
            description.appendText("with alpha value: ")
        }

    }

}

enum class AlertDialogButton(@IdRes val resId: Int) {
    POSITIVE(android.R.id.button1),
    NEGATIVE(android.R.id.button2),
    NEUTRAL(android.R.id.button3)
}

private fun clickOnButtonInAlertDialog(button: AlertDialogButton) {
    onView(withId(button.resId)).perform(click())
}

/**
 * https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/RecyclerViewMatcher.java
 */
class RecyclerViewMatcher(private val recyclerViewId: Int) {

    companion object {

        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher =
                RecyclerViewMatcher(recyclerViewId)

    }

    fun atPosition(position: Int): Matcher<View> = atPositionOnView(position, -1)

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            internal var resources: Resources? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                resources?.let { resources ->
                    idDescription =
                            try {
                                resources.getResourceName(recyclerViewId)
                            } catch (e: Resources.NotFoundException) {
                                String.format("%s (resource name not found)", recyclerViewId)
                            }
                }
                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {

                this.resources = view.resources

                var childView: View? = null
                val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                if (recyclerView.id == recyclerViewId) {
                    childView = recyclerView.layoutManager?.findViewByPosition(position)
                }

                if (childView == null) {
                    return false
                }

                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView.findViewById<View>(targetViewId)
                    view === targetView
                }
            }

        }
    }

}

fun assertSwipeToRefreshState(@IdRes resId: Int,
                              shouldBeRefreshing: Boolean) {
    onView(withId(resId))
            .check(matches(
                    if (shouldBeRefreshing) {
                        isRefreshing()
                    } else {
                        not(isRefreshing())
                    }
            ))
}

private fun isRefreshing() = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("is a SwipeRefreshLayout that is currently refreshing")
    }

    override fun matchesSafely(item: View?) =
            (item as? SwipeRefreshLayout)?.isRefreshing ?: false

}
