package com.example.qr_dasher;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.anything;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;


import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class eventIntentTests {
    @Before
    public void launchActivity(){
        ActivityScenario.launch(HomePage.class);
    }

    @Before
    public void createUserProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.name_edit)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email_edit)).perform(ViewActions.typeText("johndoe@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.details_edit)).perform(ViewActions.typeText("User details"), ViewActions.closeSoftKeyboard());

        // Simulate clicking the Create Profile button
        Espresso.onView(ViewMatchers.withId(R.id.upload_button)).perform(ViewActions.click());

        // Check if the RolePage activity is launched after creating the profile
        Espresso.onView(ViewMatchers.withId(R.id.organizer_button)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    );
    /**
     * Creates an event for a test user profile.
     * Then checks if the event was added to created events listview on organizer dashboard.
     *
     * @throws AssertionError if the item count is not equal to 1 or if the item text does not match the expected text.
     */
    @Test
    public void createEvent(){

        Espresso.onView(ViewMatchers.withId(R.id.organizer_button)).perform(ViewActions.click());
        // Checking if organizer dashboard is launched after pressing this button
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // Click create event button
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).perform(ViewActions.click());
        // Checking if organizer create event activity is launched after pressing
        Espresso.onView(ViewMatchers.withId(R.id.generateQRandCreateEvent)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // if this button is displayed > we can fill in the remaining details

        Espresso.onView(ViewMatchers.withId(R.id.eventName))
                .perform(ViewActions.typeText("Test Event Title"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.details))
                .perform(ViewActions.typeText("Test Event Details"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.pickTime)).perform(ViewActions.click());

        // Set a date (e.g., 2024-04-10) in the date picker dialog
        CustomViewActions.setDateInDatePicker(2024, Calendar.APRIL, 25);

        // Click on the OK button in the date picker dialog
        Espresso.onView(withText("OK")).perform(ViewActions.click());

        // Now, set the time in the TimePicker dialog

        // Set a time (e.g., 3:30 PM) in the time picker dialog
        CustomViewActions.setTimeInDatePicker(15, 30); // 15:30 is equivalent to 3:30 PM in 24-hour format

        // Click on the OK button in the time picker dialog
        Espresso.onView(withText("OK")).perform(ViewActions.click());

        // Once we get the details we can create the event
        Espresso.onView(ViewMatchers.withId(R.id.generateQRandCreateEvent)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.generatePromotionalQR)).perform(ViewActions.click());

        Espresso.pressBack();
        // go back
        // wait for 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.eventListView)).check((view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            ListView listView = (ListView) view;
            int itemCount = listView.getAdapter() != null ? listView.getAdapter().getCount() : 0;

            // Check if the item count is equal to 1
            if (itemCount != 1) {
                throw new AssertionError("Expected item count: 1, Actual item count: " + itemCount);
            }
            String itemText = listView.getAdapter().getItem(0).toString();
            // or if it does not match the expected title

            String expectedText = "Test Event Title";
            if (!itemText.equals(expectedText)) {
                throw new AssertionError("Expected text: " + expectedText + ", Actual text: " + itemText);
            }
        });

    }
    /**
     * Creates an event for a test user profile.
     * Then checks if the event was added to created events listview on organizer dashboard.
     *
     * @throws AssertionError if the item count is not equal to 1 or if the item text does not match the expected text.
     */
    // Incomplete TO-DO: complete this test
    @Test
    public void joinEvent() {
        Espresso.onView(ViewMatchers.withId(R.id.organizer_button)).perform(ViewActions.click());
        // Checking if organizer dashboard is launched after pressing this button
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // Click create event button
        Espresso.onView(ViewMatchers.withId(R.id.createEventButton)).perform(ViewActions.click());
        // Checking if organizer create event activity is launched after pressing
        Espresso.onView(ViewMatchers.withId(R.id.generateQRandCreateEvent)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // if this button is displayed > we can fill in the remaining details

        Espresso.onView(ViewMatchers.withId(R.id.eventName))
                .perform(ViewActions.typeText("Test Event Title"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.details))
                .perform(ViewActions.typeText("Test Event Details"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.pickTime)).perform(ViewActions.click());

        // Set a date (e.g., 2024-04-10) in the date picker dialog
        CustomViewActions.setDateInDatePicker(2024, Calendar.APRIL, 25);

        // Click on the OK button in the date picker dialog
        Espresso.onView(withText("OK")).perform(ViewActions.click());

        // Now, set the time in the TimePicker dialog

        // Set a time (e.g., 3:30 PM) in the time picker dialog
        CustomViewActions.setTimeInDatePicker(15, 30); // 15:30 is equivalent to 3:30 PM in 24-hour format

        // Click on the OK button in the time picker dialog
        Espresso.onView(withText("OK")).perform(ViewActions.click());

        // Once we get the details we can create the event
        Espresso.onView(ViewMatchers.withId(R.id.generateQRandCreateEvent)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.generatePromotionalQR)).perform(ViewActions.click());

        Espresso.pressBack();
        // go back
        // wait for 2 seconds
        Espresso.pressBack();
        // Now signing up for the event

        Espresso.onView(ViewMatchers.withId(R.id.attendee_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.browseEvents)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        Espresso.onView(ViewMatchers.withId(R.id.browseEvents)).perform(ViewActions.click());

//        onView(withId(R.id.browseEventList)).check((view, noViewFoundException) -> {
//            if (noViewFoundException != null) {
//                throw noViewFoundException;
//            }
//
//            ListView listView = (ListView) view;
//
//
//            String itemText = listView.getAdapter().getItem(0).toString();
//
//            String expectedText = "Test Event Title";
//            if (!itemText.equals(expectedText)) {
//                throw new AssertionError("Expected text: " + expectedText + ", Actual text: " + itemText);
//            }
//        });
//        onData(anything())
//                .inAdapterView(withId(R.id.browseEventList))
//                .atPosition(0)
//                .perform(click());
//        onData(allOf(is(instanceOf(String.class)), withItemContent("Test Event Title")))
//                .inAdapterView(withId(R.id.browseEventList))
//                .perform(click());
//        Espresso.onView(ViewMatchers.withId(R.id.browseEventList))
//                .perform(RecyclerViewActions.actionOnItem(
//                        hasDescendant(withText("Test Event Title")),
//                        ViewActions.click()
//                ));
    }
    }


