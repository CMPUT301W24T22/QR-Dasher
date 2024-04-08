package com.example.qr_dasher;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class createEventIntentTest {
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

    // now we are at the role page
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.eventListView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


    }