package com.example.qr_dasher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {
    public static Matcher<Object> withItemText(String expectedText) {
        return new TypeSafeMatcher<Object>() {
            @Override
            protected boolean matchesSafely(Object item) {
                // Check if the item is a String and if it matches the expected text
                return item instanceof String && ((String) item).equals(expectedText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with item text: " + expectedText);
            }
        };
    }
}