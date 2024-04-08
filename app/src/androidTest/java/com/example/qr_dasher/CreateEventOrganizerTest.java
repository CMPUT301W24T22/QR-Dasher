package com.example.qr_dasher;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateEventOrganizerTest {
    @Before
    public void launchActivity(){
        ActivityScenario.launch(CreateEventOrganizer.class);
    }
    @Test
    public void createEvent(){
        Espresso.onView(ViewMatchers.withId(R.id.eventName)).perform(ViewActions.typeText("Test Event"));
        Espresso.onView(ViewMatchers.withId(R.id.details)).perform(ViewActions.typeText("This is a test event."));
        Espresso.onView(ViewMatchers.withId(R.id.max_attendees)).perform(ViewActions.typeText("100"));
        Espresso.onView(ViewMatchers.withId(R.id.textDateTime)).perform(ViewActions.click());
    }
}
