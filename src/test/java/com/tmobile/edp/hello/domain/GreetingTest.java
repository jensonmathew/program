package com.tmobile.edp.hello.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by SSajjad1 on 3/8/2017.
 */
public class GreetingTest {

    @Test
    public void GreetingGettersTest() throws Exception{
        Greeting greeting = new Greeting(12, "Hello");
        assertEquals( greeting.getId(), 12);
        assertEquals( greeting.getGreeting(), "Hello");
    }
}
