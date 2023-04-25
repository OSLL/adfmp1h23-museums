package com.itmo.museum

import android.os.Build
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.itmo.museum.data.MuseumDataProvider
import com.itmo.museum.data.MuseumDatabase
import com.itmo.museum.models.Museum
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule

@ExperimentalComposeUiApi
abstract class AbstractMuseumUITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    val context
        get() = composeTestRule.activity

    lateinit var museums: List<Museum>

    private val database by lazy {
        MuseumDatabase.getDatabase(context)
    }

    @Before
    open fun setup() {
        museums = MuseumDataProvider.defaultProvider(context).museums

        // give location access permission if requested at the app startup
        grantLocationPermission()

        prepareDatabase()
    }

    private fun prepareDatabase() = runBlocking {
        database.reviewDao().deleteAll()
        database.museumDao().markAllAsNotVisited()
    }

    private fun grantLocationPermission() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = UiDevice.getInstance(instrumentation).findObject(
                UiSelector().text(
                    when {
                        Build.VERSION.SDK_INT == 23 -> "Allow"
                        Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                        Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                        else -> "While using the app"
                    }
                )
            )
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }
    }
}

@ExperimentalComposeUiApi
abstract class AbstractMuseumUiTestWithLogin : AbstractMuseumUITest() {
    @Before
    override fun setup() {
        super.setup()

        // login as an anonymous user
        composeTestRule
            .onNodeWithTag(context.getString(R.string.skip_login_button))
            .performClick()
    }
}
