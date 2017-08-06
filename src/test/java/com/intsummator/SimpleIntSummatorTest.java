package com.intsummator;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SimpleIntSummator}.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class SimpleIntSummatorTest {

    @Test
    public void calculateSum() throws Exception {
        SimpleIntSummator intSummator = new SimpleIntSummator();

        assertEquals(15, intSummator.calculateSum(Paths.get("simple.txt")));
    }

}