package com.tmobile.hello.controller;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.junit.Assert.*;
import org.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.env.MockEnvironment;

@PrepareForTest({System.class})
public class SpringBootHelloWorldWebControllerTest {


	@Test
    public void testGetDeployStats() throws Exception {
		MockEnvironment env = new MockEnvironment();
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController(env);
		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
    }

	@Test
    public void testGetDeployStatsTKE() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("DEPLOY_PLATFORM", "TKE");
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController(env);

		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
		assertEquals("TKE", mavResp.getModel().get("deployPlatform"));

	}

	@Test
    public void testGetDeployStatsConducktor() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("DEPLOY_PLATFORM", "Conducktor");
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController(env);

		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
		assertEquals("Conducktor", mavResp.getModel().get("deployPlatform"));
	}

	@Test
    public void testGetDeployStatsPCF() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("DEPLOY_PLATFORM", "PCF");
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController(env);

		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
		assertEquals("PCF", mavResp.getModel().get("deployPlatform"));
		assertEquals("unset", mavResp.getModel().get("podEnvInfo"));
	}

	@Test
    public void testGetDeployStatsPCFWithPodInfo() throws Exception {
		MockEnvironment env = new MockEnvironment();
		env.setProperty("DEPLOY_PLATFORM", "PCF");
		JSONObject podInfoJson = new JSONObject();
		podInfoJson.put("organization_name","tmo-test");
		podInfoJson.put("application_name","helloworld-test");
		podInfoJson.put("space_name","test");
		env.setProperty("VCAP_APPLICATION", podInfoJson.toString());
		SpringBootHelloWorldWebController webhc = new SpringBootHelloWorldWebController(env);

		ModelAndView mavResp = webhc.getDeployStats();
		assertNotNull(mavResp);
		assertEquals("PCF", mavResp.getModel().get("deployPlatform"));
		assertEquals(podInfoJson.getString("organization_name"), mavResp.getModel().get("podOrgName"));
		assertEquals(podInfoJson.getString("application_name"), mavResp.getModel().get("appName"));
		assertEquals(podInfoJson.getString("space_name"), mavResp.getModel().get("podNamespace"));
	}

}	
