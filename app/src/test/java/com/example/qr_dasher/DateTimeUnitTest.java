package com.example.qr_dasher;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test class for the DateTime class.
 * Tests constructors, getters, and setters for the DateTime class.
 */
public class DateTimeUnitTest {

    /**
     * Tests the constructor and getters of the DateTime class.
     * Verifies if the constructor sets the attributes correctly and if the getters return the expected values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Create a DateTime object
        DateTime dateTime = new DateTime(2024, 3, 28, 14, 30);

        // Check if the getters return the expected values
        assertEquals(2024, dateTime.getYear());
        assertEquals(3, dateTime.getMonth());
        assertEquals(28, dateTime.getDay());
        assertEquals(14, dateTime.getHour());
        assertEquals(30, dateTime.getMinute());
    }

    /**
     * Tests the setters of the DateTime class.
     * Checks if the setters modify the attributes correctly and if the getters return the updated values after using the setters.
     */
    @Test
    public void testSetters() {
        // Create a DateTime object
        DateTime dateTime = new DateTime();

        // Set values using setters
        dateTime.setYear(2025);
        dateTime.setMonth(4);
        dateTime.setDay(15);
        dateTime.setHour(10);
        dateTime.setMinute(45);

        // Check if the getters return the updated values
        assertEquals(2025, dateTime.getYear());
        assertEquals(4, dateTime.getMonth());
        assertEquals(15, dateTime.getDay());
        assertEquals(10, dateTime.getHour());
        assertEquals(45, dateTime.getMinute());
    }
}
