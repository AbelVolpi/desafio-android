import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.userlist.MainActivity
import com.picpay.desafio.android.utils.MockServerDispatcher
import com.picpay.desafio.android.utils.RecyclerViewMatchers
import com.picpay.desafio.android.utils.waitUntil
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var robot: MainActivityRobot
    private val server = MockWebServer()

    @Before
    fun setup() {
        server.start(8080)
        server.dispatcher = MockServerDispatcher().RequestDispatcher()
        robot = MainActivityRobot()
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun shouldDisplayTitle() {
        robot.launch()
            .checkTitleIsDisplayed()
    }

    @Test
    fun shouldDisplayListItem() {
        robot.launch()
            .checkListIsDisplayed()
            .checkUserAtPosition(0, "Name1")
            .checkUserAtPosition(1, "Name2")
            .checkUserAtPosition(2, "Name3")
    }
}

class MainActivityRobot {
    fun launch() = apply {
        launchActivity<MainActivity>()
    }

    fun checkTitleIsDisplayed() = apply {
        val expectedTitle =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.title)
        onView(withText(expectedTitle)).check(matches(isDisplayed()))
    }

    fun checkListIsDisplayed() = apply {
        onView(withId(R.id.recyclerView)).perform(waitUntil(isDisplayed()))
    }

    fun checkUserAtPosition(position: Int, userName: String) = apply {
        RecyclerViewMatchers.checkRecyclerViewItem(
            R.id.recyclerView,
            position,
            withText(userName)
        )
    }
}
