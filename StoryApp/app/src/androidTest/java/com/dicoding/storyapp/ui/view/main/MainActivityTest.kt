package com.dicoding.storyapp.ui.view.main

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.storyapp.EspressoIdlingResource
import com.dicoding.storyapp.R
import org.hamcrest.CoreMatchers.anyOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLogoutLoginProcess_Success() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(anyOf(withId(R.id.menu_logout), withText(R.string.menu_logout))).perform(click())
        onView(withId(R.id.ed_login_email)).perform(typeText("wendy@gmail.com"), pressImeActionButton())
        onView(withId(R.id.ed_login_password)).perform(typeText("wendy123"), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())

    }
}