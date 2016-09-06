package com.letit0or1.dayscheduleview;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        Time time0 = new Time(5, 5);


        Time time1 = new Time(5, 5);
        time1.setHour(20);

        Time time2 = new Time(5, 5);
        time2.setHour(6);
        time2.setMinute(70);

        System.out.println(
                time0.toString() + "\n" +
                        time1.toString() + "\n" +
                        time2.toString()
        );
    }
}