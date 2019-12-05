package com.example.karthickmadasamy.myapplication.view.activity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.example.karthickmadasamy.myapplication.R;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

/**
 * Created by Karthick.Madasamy on 12/5/2019.
 * Instrumented unit tests are tests that run on physical devices and emulators.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    private MockWebServer server;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        server = new MockWebServer();
        server.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        server.url("/").toString();
    }

    @Test
    public void testListViewIsShown() throws Exception {
        String fileName = "feeder_success_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestServiceHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));

        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.feeder_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testRefreshButtonIsShown() throws Exception {
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.refresh)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

       @Test
    public void testListViewItemClickRow() throws Exception {
        String fileName = "feeder_success_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestServiceHelper.getStringFromFile(getInstrumentation().getContext(), fileName)));
        Intent intent = new Intent();
        mActivityRule.launchActivity(intent);
        onView(withId(R.id.feeder_view)).check(matches(hasDescendant(withText(endsWith("Beavers")))));
        onView(withId(R.id.feeder_view)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(endsWith("Beavers"))), click()));
    }


    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}