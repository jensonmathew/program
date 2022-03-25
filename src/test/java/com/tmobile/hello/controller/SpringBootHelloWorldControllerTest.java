package com.tmobile.hello.controller;

import com.tmobile.hello.domain.SecurityUserDetails;
import com.tmobile.hello.repository.model.Featureflags;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.junit.Assert.*;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@PrepareForTest({System.class})
public class SpringBootHelloWorldControllerTest {

	@Test
    public void testHome() throws Exception {
		MockEnvironment env = new MockEnvironment();
		SpringBootHelloWorldController hc = new SpringBootHelloWorldController(env);
		String result = hc.home(() -> null);

		JSONObject jo = new JSONObject();
		jo.put("Message","Hello World!");

		assertEquals(result,jo.toString());
    }

	@Test
	public void testGetEnv() throws Exception {
		MockEnvironment env = new MockEnvironment();
		SpringBootHelloWorldController hc = new SpringBootHelloWorldController(env);
		assertNotNull(hc.getEnv("PATH"));
		assertEquals(new JSONObject().toString(), hc.getEnv("THIS_ENV_VARIABLE_KEY_SHOULD_NOT_BE_SET"));
	}

	@Test
	public void testGetProp() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("TEST_KEY", "TEST_VALUE");
		env.setProperty("unleash.instance.id", "PpzzkRXXSs5nokqqsrDc");
		env.setProperty("unleash.instance.api.url", "https://gitlab.com/api/v4/feature_flags/unleash/12679731");

		SpringBootHelloWorldController hc = new SpringBootHelloWorldController(env);

		assertEquals("{}",hc.getProp("this.is.a.test.property.that.should.never.be.set"));

		JSONObject jo = new JSONObject();
		jo.put("TEST_KEY","TEST_VALUE");

		assertEquals(jo.toString(),hc.getProp("TEST_KEY"));
	}

	@Test
	public void testGetFF() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("unleash.instance.id", "PpzzkRXXSs5nokqqsrDc");
		env.setProperty("unleash.instance.api.url", "https://gitlab.com/api/v4/feature_flags/unleash/12679731");

		SpringBootHelloWorldController hc = new SpringBootHelloWorldController(env);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Featureflags ff = new Featureflags();
		ff.setUser("Test");
		ff.setPassword("Test");
		ff.setEmail("test@test.com");
		ff.setId(1);
		ff.setRoles("user");
		SecurityUserDetails p = new SecurityUserDetails(ff);

		Mockito.when(authentication.getPrincipal()).thenReturn(p);

		JSONObject jo = new JSONObject();
		jo.put("this.is.a.test.ff.string","false");
		assertEquals(jo.toString(), hc.getFF("this.is.a.test.ff.string"));

		jo.remove("this.is.a.test.ff.string");
		jo.put("awesomefeature","false");
		assertEquals(jo.toString(), hc.getFF("awesomefeature"));
	}
}