package com.tmobile.hello.controller;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.junit.Assert.*;

import org.springframework.web.servlet.ModelAndView;

@PrepareForTest({System.class})
public class SpringBootHelloWorldWebControllerTest {


	@Test
    public void testGetDeployStats() throws Exception {
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController();
		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
    }

}	
