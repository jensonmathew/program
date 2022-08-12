package com.tmobile.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringBootHelloWorldWebController {

    @GetMapping("/deploy/info")
    public ModelAndView getDeployStats() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("status",("active"));
        mav.addObject("deployEnv",parseEnvVariable("ENVIRONMENT"));
        mav.addObject("commitSha",parseEnvVariable("COMMIT_SHA"));
        mav.addObject("appName",parseEnvVariable("APP_NAME"));
        mav.addObject("deployedBy",parseEnvVariable("DEPLOYED_BY"));
        mav.addObject("deployedVersion",parseEnvVariable("DEPLOYED_VERSION"));
        mav.addObject("commitBranch",parseEnvVariable("COMMIT_BRANCH"));
        mav.addObject("commitUser",parseEnvVariable("COMMIT_USER"));
        mav.addObject("commitTimeStamp",parseEnvVariable("COMMIT_TS"));
        mav.addObject("targetHosturl",parseEnvVariable("TARGET_SERVER_URL"));
        mav.addObject("deployStrategy",parseEnvVariable("DEPLOY_STRATEGY"));
        mav.addObject("podName",parseEnvVariable("POD_NAME"));
        mav.addObject("podIp",parseEnvVariable("POD_IP"));
        mav.addObject("podNamespace",parseEnvVariable("POD_NAMESPACE"));
        mav.addObject("nodeName",parseEnvVariable("NODE_NAME"));
        mav.addObject("podHostIp",parseEnvVariable("POD_HOST_IP"));
        mav.addObject("podSvcAccount",parseEnvVariable("POD_SERVICE_ACCOUNT"));  
        mav.setViewName("welcome");
        return mav;
    }

    private String parseEnvVariable(String key){
        String keyVal = null;
        keyVal=System.getenv(key);
        if (keyVal == null || keyVal.isEmpty()) {
            keyVal = "unset";
        } 
        return keyVal.trim();
    }

}

