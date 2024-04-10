package com.example.qr_dasher;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class adminViewIntentTest {
    @Before
    public void launchActivity(){
        ActivityScenario.launch(HomePage.class);
    }

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    );

    @Test
    public void loginAsAnAdmin(){
        Espresso.onView(withId(R.id.next_button)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.admin_button)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.enter_pin))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Enter "QRDasher" into the PIN field
        Espresso.onView(withId(R.id.enter_pin))
                .perform(ViewActions.typeText("QRDasher"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.enter_button))
                .perform(ViewActions.click());


        Espresso.onView(withId(R.id.user_list_button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.pressBack();

        Espresso.onView(withId(R.id.event_list_button))
                .perform(ViewActions.click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
