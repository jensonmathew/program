package com.tmobile.edp.hello.controller;

import com.tmobile.edp.hello.domain.Greeting;
import org.junit.Assert.*;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by SSajjad1 on 3/8/2017.
 */
public class HelloWorldControllerTest {

    @Test
    public void testController() throws Exception {
        HelloWorldController controller = new HelloWorldController();
        Greeting greeting = controller.sayHello("Moto");
        assertNotNull(greeting);
        assertEquals(greeting.getGreeting(), "Hello, Moto!");
    }
}
