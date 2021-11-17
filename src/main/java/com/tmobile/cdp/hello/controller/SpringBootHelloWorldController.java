package com.tmobile.cdp.hello.controller;

import java.security.Principal;
import com.tmobile.cdp.hello.domain.SecurityUserDetails;
import com.tmobile.cdp.hello.exception.HelloworldException;
import org.json.JSONObject;
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

        if (instanceId != null && unleashAPI != null){
            UnleashConfig unleashConfig = UnleashConfig.builder()
                    .appName("all environments")
                    .instanceId(instanceId)
                    .unleashAPI(unleashAPI)
                    .build();
            unleash = new DefaultUnleash(unleashConfig);
        } else{
            System.out.println("Forfeiting Unleash due to missing unleash.instance.id or unleash.instance.api.url");
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

    // To return what the feature flag is set to
    @RequestMapping("/ff/{key}")
    public String getFF(@PathVariable("key") String key) {
        int userId = 0;
        UnleashContext uContext;
        JSONObject jo = new JSONObject();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            return jo.toString();

        Object principle = authentication.getPrincipal();
        if (principle instanceof SecurityUserDetails){
            userId = ((SecurityUserDetails) principle).getUserId();
            if (userId != 0){
                uContext = UnleashContext.builder()
                        .userId(String.valueOf(userId)).build();
                jo.put(key,String.valueOf(getUnleash().isEnabled("key",uContext)));
            }
            else {
                jo.put(key,String.valueOf(getUnleash().isEnabled("key")));
            }
        }
        return jo.toString();
    }

    // To return default hello world message to user
    @RequestMapping("/")
    String home(Principal principal) throws HelloworldException {
        JSONObject jo = new JSONObject();
        jo.put("Message","Hello World!");
        return jo.toString();
    }
}
