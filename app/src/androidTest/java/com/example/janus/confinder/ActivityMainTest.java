package com.example.janus.confinder;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Checks.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
// TODO Add more tests. Apparently, Espresso does not work with Google Maps. So
// TODO I need to find a framework that does.
// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1


@RunWith(AndroidJUnit4.class)
public class ActivityMainTest {

    @Rule
    public ActivityTestRule<ConventionFinderActivity> mActivityRule = new ActivityTestRule<>(
            ConventionFinderActivity.class);

    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void testMainActivityExists() throws Exception {
        checkNotNull(mActivityRule.getActivity());

    }

    @Test
    public void testFirstMessage() throws Exception {

        onView(withText("Press START to Begin Con Search"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.searchButton_AlertDialog)).perform(click());

    }

    @After
    public void tearDown() throws Exception {
    }
}