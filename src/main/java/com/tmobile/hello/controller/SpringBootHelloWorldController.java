package com.tmobile.hello.controller;

import java.security.Principal;

import ch.qos.logback.classic.Logger;
import com.tmobile.hello.SpringBootHelloWorld;
import com.tmobile.hello.domain.SecurityUserDetails;
import com.tmobile.hello.exception.HelloworldException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.UnleashContext;
import no.finn.unleash.util.UnleashConfig;
import org.springframework.core.env.Environment;


@RestController
public class SpringBootHelloWorldController {

    // Logger
    private org.slf4j.Logger LOGGER= LoggerFactory.getLogger(SpringBootHelloWorldController.class);

    // To pull from application.properties
    @Autowired
    private Environment env;

    // To use gitlab feature flags
    private Unleash unleash;

    public SpringBootHelloWorldController(Environment environment){
        env = environment;
        getUnleash();
    }

    // Instantiate unleash if not already set (concurrent = 1)
    private synchronized void newUnLeash () {
        String instanceId = env.getProperty("unleash.instance.id");
        String unleashAPI = env.getProperty("unleash.instance.api.url");

        String environment = System.getenv("ENVIRONMENT");
        if (environment == null || environment.isEmpty()) {
            environment = "unset";
        }
        LOGGER.info("ENVIRONMENT set to:" + environment);

        if (instanceId != null && unleashAPI != null){
            UnleashConfig unleashConfig = UnleashConfig.builder()
                    .appName(environment)
                    .instanceId(instanceId)
                    .unleashAPI(unleashAPI)
                    .build();
            unleash = new DefaultUnleash(unleashConfig);
        } else{
            LOGGER.error("Forfeiting Unleash due to missing unleash.instance.id or unleash.instance.api.url");
        }
    }

    // Return global unleash object if already instantiated
    public Unleash getUnleash(){
        if (unleash == null)
            newUnLeash();
        return unleash;
    }

    // To return what the environment variable on the server is set to
    @RequestMapping("/env/{key}")
    public String getEnv(@PathVariable("key") String key) {
        String value = System.getenv(key);
        if (value != null) {
            JSONObject jo = new JSONObject();
            jo.put(key,value);
            return jo.toString();
        }else{
            return new JSONObject().toString();
        }
    }

    // To return what the spring environment property is set to
    // automatically determining application.properties is being used
    @RequestMapping("/prop/{key}")
    public String getProp(@PathVariable("key") String key) {
        String value = env.getProperty(key);
        if (value != null) {
            JSONObject jo = new JSONObject();
            jo.put(key,value);
            return jo.toString();
        }else{
            return new JSONObject().toString();
        }
    }

    private UnleashContext getCurrentContext(){
        UnleashContext uContext;
        int userId=getCurrentUser();
        if (userId != 0) {
            uContext = UnleashContext.builder()
                    .userId(String.valueOf(userId)).build();
        } else {
            uContext = UnleashContext.builder().build();
        }

        return uContext;
    }

    private int getCurrentUser() {
        int userId = 0;
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        Object principle = authentication.getPrincipal();
        if (principle instanceof SecurityUserDetails){
            userId = ((SecurityUserDetails) principle).getUserId();
        }

        return userId;
    }
    // To return what the feature flag is set to
    @RequestMapping("/ff/{key}")
    public String getFF(@PathVariable("key") String key) {
        JSONObject jo = new JSONObject();
        jo.put(key,String.valueOf(getUnleash().isEnabled("key",getCurrentContext())));
        return jo.toString();
    }

    // To return default hello world message to user
    @RequestMapping("/")
    String home(Principal principal) throws HelloworldException {
        JSONObject jo = new JSONObject();
        if (getUnleash() != null && getUnleash().isEnabled("french", getCurrentContext())) {
            jo.put("Message","Bonjour le monde!");
        } else if (getUnleash() != null && getUnleash().isEnabled("german", getCurrentContext())){
            jo.put("Message","Hallo Welt!");
        } else if (getUnleash() != null && getUnleash().isEnabled("dutch", getCurrentContext())){
            jo.put("Message","Hallo Wereld!\n");
        }
        else{
            jo.put("Message","Hello World!");
        }
        return jo.toString();
    }

    @RequestMapping("/api/deploy/info")
    String showStatus(Principal principal) throws HelloworldException {
        JSONObject jo = new JSONObject();
        jo.put("status","active");
        jo.put("deployEnv",parseEnvVariable("ENVIRONMENT"));
        jo.put("commitSha",parseEnvVariable("COMMIT_SHA"));
        jo.put("appName",parseEnvVariable("APP_NAME"));
        jo.put("deployedBy",parseEnvVariable("DEPLOYED_BY"));
        jo.put("deployedVersion",parseEnvVariable("DEPLOYED_VERSION"));
        jo.put("commitBranch",parseEnvVariable("COMMIT_BRANCH"));
        jo.put("commitUser",parseEnvVariable("COMMIT_USER"));
        jo.put("commitTimeStamp",parseEnvVariable("COMMIT_TS"));
        jo.put("targetHosturl",parseEnvVariable("TARGET_SERVER_URL"));
        jo.put("deployStrategy",parseEnvVariable("DEPLOY_STRATEGY"));
        jo.put("podName",parseEnvVariable("POD_NAME"));
        jo.put("podIp",parseEnvVariable("POD_IP"));
        jo.put("podNamespace",parseEnvVariable("POD_NAMESPACE"));
        jo.put("nodeName",parseEnvVariable("NODE_NAME"));
        jo.put("podHostIp",parseEnvVariable("POD_HOST_IP"));
        jo.put("podSvcAccount",parseEnvVariable("POD_SERVICE_ACCOUNT"));
        return jo.toString();
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
