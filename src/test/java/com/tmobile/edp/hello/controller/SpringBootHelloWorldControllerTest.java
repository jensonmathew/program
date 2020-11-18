package com.tmobile.edp.hello.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.tmobile.edp.hello.exception.HelloworldException;

import no.finn.unleash.Unleash;
import no.finn.unleash.UnleashContext;

@RunWith(MockitoJUnitRunner.class)
public class SpringBootHelloWorldControllerTest {
	
	@InjectMocks
	SpringBootHelloWorldController springBootHelloWorldController;
	
	@Mock
	Unleash unleash;

	@Test
	public void testHome() throws HelloworldException {
		Mockito.when(unleash.isEnabled(Matchers.any())).thenReturn(true);
		Mockito.when(unleash.isEnabled(Matchers.any(), Matchers.any(UnleashContext.class))).thenReturn(true);
		Principal principal = new Principal() {
			
			@Override
			public String getName() {
				return "someEmail@email.com";
			}
		};
		assertNotNull(springBootHelloWorldController.home(principal));
	}

	@Test
	public void testHomeFalse() throws HelloworldException {
		Mockito.when(unleash.isEnabled(Matchers.any())).thenReturn(false);
		Mockito.when(unleash.isEnabled(Matchers.any(), Matchers.any(UnleashContext.class))).thenReturn(false);
		Principal principal = new Principal() {
			
			@Override
			public String getName() {
				return "someEmail@email.com";
			}
		};
		assertNotNull(springBootHelloWorldController.home(principal));
	}
	
	@Test(expected = Exception.class)
	public void testHomeException() {
		Mockito.when(unleash.isEnabled(Matchers.any())).thenThrow(Exception.class);
	}
}