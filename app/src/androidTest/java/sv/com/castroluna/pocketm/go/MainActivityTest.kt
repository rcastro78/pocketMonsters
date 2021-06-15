package sv.com.castroluna.pocketm.go
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

import sv.com.castroluna.pocketm.go.R.id.*

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun isViewPagerVisible(){
        Espresso.onView(withId(container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun areTabsVisible(){
        Espresso.onView(withId(tabs))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun isMapVisible(){
        Espresso.onView(withId(map))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMapClickable(){
        Espresso.onView(withId(map))
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
    }
}