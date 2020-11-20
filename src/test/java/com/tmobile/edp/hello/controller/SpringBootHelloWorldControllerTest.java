package com.tmobile.edp.hello.controller;

import static org.junit.Assert.assertNotNull;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.tmobile.edp.hello.exception.HelloworldException;

import no.finn.unleash.Unleash;
import no.finn.unleash.UnleashContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class})
public class SpringBootHelloWorldControllerTest {
	
	@InjectMocks
	SpringBootHelloWorldController springBootHelloWorldController;
	
	@Mock
	Unleash unleash;
	
	@Before
    public void executedBeforeEach() {
		MockitoAnnotations.initMocks(this);
    }

	@Test
	public void testHome() throws HelloworldException {
		PowerMockito.mockStatic(System.class);
		PowerMockito.when(System.getenv("ENVIRONMENT")).thenReturn("env");
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.any())).thenReturn(true);
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.any(), ArgumentMatchers.any(UnleashContext.class))).thenReturn(true);
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.any())).thenReturn(true);
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
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.anyString())).thenReturn(false);
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.any(), ArgumentMatchers.any(UnleashContext.class))).thenReturn(false);
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
		PowerMockito.when(unleash.isEnabled(ArgumentMatchers.any())).thenThrow(Exception.class);
	}
}