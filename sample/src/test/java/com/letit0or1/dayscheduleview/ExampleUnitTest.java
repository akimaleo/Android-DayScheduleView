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
        int i = 10;

        System.out.println(getTimeByPx(60) + "");

        while (true) {
            i += 10;
            Thread.sleep(500);
            System.out.println(getTimeByPx(i) + "");
        }
    }

    private float getTimeByPx(int px) {
        float step = 60;
        float pxMin = step / 60;
        return px / pxMin;
    }
}