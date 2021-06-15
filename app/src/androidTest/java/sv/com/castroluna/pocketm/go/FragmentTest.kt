package sv.com.castroluna.pocketm.go

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith
import sv.com.castroluna.pocketm.go.fragment.*

@RunWith(AndroidJUnit4::class)
class FragmentTest {
    @Test
    fun testCapturedByOtherFragment() {
        val scenario = launchFragmentInContainer<CapturedByOtherFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)

    }
    @Test
    fun testCommunityFragment() {
        val scenario = launchFragmentInContainer<CommunityFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)

    }

    @Test
    fun testCapturedFragment() {
        val scenario = launchFragmentInContainer<CapturedFragment>()
        scenario.moveToState(Lifecycle.State.CREATED)

    }


    @Test fun testGetCapturedPokemonData() {
        val scenario = launchFragmentInContainer<CapturedFragment>()
        scenario.onFragment { fragment ->
            CoroutineScope(Dispatchers.IO).launch {
                fragment.readData()
            }

        }
    }

    @Test fun testGetMyTeam() {
        val scenario = launchFragmentInContainer<MyTeamFragment>()
        scenario.onFragment { fragment ->
            CoroutineScope(Dispatchers.IO).launch {
                fragment.readData()
            }

        }
    }

}