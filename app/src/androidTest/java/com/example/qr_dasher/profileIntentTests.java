package com.example.qr_dasher;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class profileIntentTests {
    @Before
    public void launchActivity(){
        ActivityScenario.launch(HomePage.class);
    }
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    );

    /**
     * This test first creates a guest profile and checks what roles they can take (only attendee)
     * And once they create profile, they should have full access .i.e, can be either organizer or attendee
     *
     *
     * @throws AssertionError If anything goes unexpected in the tests.
     */    @Test
    public void GuestToUserSwitch(){
         // Skip sign up and continue as a guest
        Espresso.onView(ViewMatchers.withId(R.id.next_button)).perform(ViewActions.click());
         // Ensure that the next page is RolePage
        Espresso.onView(ViewMatchers.withId(R.id.attendee_button)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // Ensuring that organizer role is not displayed if guest login
        Espresso.onView(ViewMatchers.withId(R.id.organizer_button))
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));


        // Go to attendee dashboard
        Espresso.onView(ViewMatchers.withId(R.id.attendee_button)).perform(ViewActions.click());

        // click edit profile
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_button)).perform(ViewActions.click());
        // Ensure we are at editProfile
        Espresso.onView(ViewMatchers.withId(R.id.name_edit)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        // add test values
        Espresso.onView(ViewMatchers.withId(R.id.name_edit)).perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.email_edit)).perform(ViewActions.typeText("johndoe@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.details_edit)).perform(ViewActions.typeText("User details"), ViewActions.closeSoftKeyboard());
        // Click update button
        Espresso.onView(ViewMatchers.withId(R.id.upload_button)).perform(ViewActions.click());
        Espresso.pressBack();
        // Role selection page
        // Now organizer should be visible
        Espresso.onView(ViewMatchers.withId(R.id.organizer_button)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.onView(ViewMatchers.withId(R.id.organizer_button)).perform(ViewActions.click());

    }


}