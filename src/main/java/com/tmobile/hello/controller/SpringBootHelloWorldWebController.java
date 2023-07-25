package com.tmobile.hello.controller;

import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Controller
public class SpringBootHelloWorldWebController {

    // Logger
    private org.slf4j.Logger LOGGER= LoggerFactory.getLogger(SpringBootHelloWorldController.class);

    // To pull from application.properties
    @Autowired
    private Environment env;

    public SpringBootHelloWorldWebController(Environment environment){
        env = environment;
    }

    @GetMapping("/deploy/info")
    public ModelAndView getDeployStats() {
        StringBuilder appPlatformInfo = new StringBuilder("GitLab");
        String deployPlatform = parseEnvVariable("DEPLOY_PLATFORM");
        String deployStrategy = parseEnvVariable("DEPLOY_STRATEGY");
        appPlatformInfo.append("\t").append(deployPlatform);
        ModelAndView mav = new ModelAndView();
        mav.addObject("status","active");
        mav.addObject("smokeURL",parseEnvVariable("smokeURL"));
        mav.addObject("deployEnv",parseEnvVariable("ENVIRONMENT"));
        mav.addObject("commitSha",parseEnvVariable("COMMIT_SHA"));
        mav.addObject("appName",parseEnvVariable("APP_NAME"));
        mav.addObject("pipelineId","#"+parseEnvVariable("PIPELINE_ID"));
        mav.addObject("pipelineUrl",parseEnvVariable("PIPELINE_URL"));
        mav.addObject("deployedBy",parseEnvVariable("DEPLOYED_BY"));
        mav.addObject("deployedVersion",parseEnvVariable("DEPLOYED_VERSION"));
        mav.addObject("commitBranch",parseEnvVariable("COMMIT_BRANCH"));
        mav.addObject("commitUser",parseEnvVariable("COMMIT_USER"));
        mav.addObject("commitTimeStamp",parseEnvVariable("COMMIT_TS"));
        mav.addObject("targetHosturl",parseEnvVariable("TARGET_SERVER_URL"));
        mav.addObject("deployStrategy",deployStrategy);
        mav.addObject("deployPlatform",deployPlatform);
        if ("PCF".equalsIgnoreCase(deployPlatform)) {
            String appInfo=parseEnvVariable("VCAP_APPLICATION");
            if ("unset".equalsIgnoreCase(appInfo)){
                mav.addObject("podEnvInfo",appInfo);
            }else{
                JSONObject pcfObj = new JSONObject(parseEnvVariable("VCAP_APPLICATION"));
                mav.addObject("appName",pcfObj.get("application_name"));
                mav.addObject("podNamespace",pcfObj.get("space_name"));
                mav.addObject("podOrgName",pcfObj.get("organization_name"));
            }
            mav.addObject("podAddress",parseEnvVariable("CF_INSTANCE_ADDR"));
            mav.addObject("podIp",parseEnvVariable("CF_INSTANCE_INTERNAL_IP"));
            mav.addObject("podHostIp",parseEnvVariable("CF_INSTANCE_IP"));
            mav.addObject("podSvcAccount",parseEnvVariable("USER"));
        } else {
            mav.addObject("podName",parseEnvVariable("POD_NAME"));
            mav.addObject("podIp",parseEnvVariable("POD_IP"));
            mav.addObject("podNamespace",parseEnvVariable("POD_NAMESPACE"));
            mav.addObject("nodeName",parseEnvVariable("NODE_NAME"));
            mav.addObject("podHostIp",parseEnvVariable("POD_HOST_IP"));
            mav.addObject("podSvcAccount",parseEnvVariable("POD_SERVICE_ACCOUNT"));
            if (!("ROLLING_UPDATE".equalsIgnoreCase(deployStrategy))) {
                appPlatformInfo.append("\t").append("Flagger");
            }
        }
        mav.addObject("platformInfo",appPlatformInfo);
        mav.setViewName("welcome");
        return mav;
    }

    private String parseEnvVariable(String key){
        String keyVal = null;
        keyVal=System.getenv(key);
        if (keyVal == null || keyVal.isEmpty()) {
            keyVal = env.getProperty(key);
        }
        if (keyVal == null || keyVal.isEmpty()) {
            keyVal = "unset";
        }
        return keyVal.trim();
    }

}

