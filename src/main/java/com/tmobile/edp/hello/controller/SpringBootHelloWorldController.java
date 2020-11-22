package com.tmobile.edp.hello.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmobile.edp.hello.exception.HelloworldException;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.UnleashContext;
import no.finn.unleash.util.UnleashConfig;

 
@RestController
public class SpringBootHelloWorldController {
	
	private static final String LOGO_IMAGE_BASE64 = "data:image/png;base64,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";
private static final String ENVIRONMENT = "ENVIRONMENT";   
private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootHelloWorldController.class);    
    UnleashConfig config = UnleashConfig.builder()
            .appName("all environments")
            .instanceId("PpzzkRXXSs5nokqqsrDc")
            .unleashAPI("https://gitlab.com/api/v4/feature_flags/unleash/12679731")
            .build();
    Unleash unleash = new DefaultUnleash(config);

    @RequestMapping("/")
    String home(Principal principal) throws HelloworldException {
        try{
            LOGGER.info("Info {}", unleash.toString());
            String allUserMessage = getMessageForAllUser();
            boolean envAllUserMessage = getMessageForEnvAllUser();
            String userIdfeatureMessage = getMessageForUserIdFeature(principal.getName());
            return "<div style='position: absolute;top: 0;right: 150px; z-index:1'><img src='"+LOGO_IMAGE_BASE64+"'/> <a style='padding: 10px; text-decoration: none; vertical-align: middle; margin: auto 0; top: 15px; position: absolute; background: #e20074; color: white; margin-left: 40px;width: max-content;' href='\\login'>Log out</a></div>"
            		+ "<div style='padding:15px;'>Logged in user as: " + principal.getName() + "</div>"
            		+ (envAllUserMessage ? "<ul><li><div style='padding:10px;'><div style='border-radius:5px; padding:10px; margin:5px; border:1px solid #e20074; display:inline-block;'><b>Logged in env as: " + System.getenv(ENVIRONMENT) + " </b> <p>Read more about Evironment based Feature Flags.</p> </div></div></li></ul>" : "")
                            + "<ul>"
                            + "<li>"+allUserMessage+"</li>"
                            + (userIdfeatureMessage.equals("") ? "" : "<li>" + userIdfeatureMessage + "</li>")
                            +"</ul>"
                            + "<div id=\"left\"></div>\r\n"
                            + "<div id=\"right\"></div>\r\n"
                            + "<div id=\"top\"></div>\r\n"
                            + "<div id=\"bottom\"></div>"
                            + "<style>"
                            + "#top, #bottom, #left, #right {\r\n"
                            + "	background: #e20074;\r\n"
                            + "	position: fixed;\r\n"
                            + "	}\r\n"
                            + "	#left, #right {\r\n"
                            + "		top: 0; bottom: 0;\r\n"
                            + "		width: 3px;\r\n"
                            + "		}\r\n"
                            + "		#left { left: 0; }\r\n"
                            + "		#right { right: 0; }\r\n"
                            + "		\r\n"
                            + "	#top, #bottom {\r\n"
                            + "		left: 0; right: 0;\r\n"
                            + "		height: 3px;\r\n"
                            + "		}\r\n"
                            + "		#top { top: 0; }\r\n"
                            + "		#bottom { bottom: 0; }"
                            + "</style>";                    
        } catch (Exception exception) {
            LOGGER.error("ERROR", exception);
            throw new HelloworldException("ERROR", exception);
        }                   
    }
    
    private String getMessageForAllUser() {
    	String something;
        if (unleash.isEnabled("awesomefeature")) {
          	something = "<div style='padding:10px;'><div style='border-radius:5px; padding:10px; margin:5px; border:1px solid #e20074; display:inline-block;'><b>Enables the feature for all users.</b> <p>It uses the <a href='https://docs.gitlab.com/ee/operations/feature_flags.html#all-users'>default</a> Unleash activation strategy.</p></div></div>";
        } 
        else {
        	something = "<div style='padding:10px;'><div style='border-radius:5px; padding:10px; margin:5px; border:1px solid #e20074; display:inline-block;'><b>Disabled the feature for all users.</b> <p>To enable feature flag, go to Project Settings ->Operations ->Feature Flags</p> </div></div>";
        }
        return something;
    }
    

    private boolean getMessageForEnvAllUser() {

    	boolean something = false;
    	if (StringUtils.hasText(System.getenv(ENVIRONMENT))) {
        	UnleashConfig config1 = UnleashConfig.builder()
                    .appName(System.getenv(ENVIRONMENT))
                    .instanceId("PpzzkRXXSs5nokqqsrDc")
                    .unleashAPI("https://gitlab.com/api/v4/feature_flags/unleash/12679731")
                    .build();
            Unleash unleash1 = new DefaultUnleash(config1);
            if (unleash1.isEnabled("envfeature")) {
              	something = true;
            } else {
            	something = false;
            }
    	} else {
    		LOGGER.info("Cannot find the environment 'ENVIRONMENT'");
    	}
        return something;
    }
    
    private String getMessageForUserIdFeature(String userId) {
    	String something;
        UnleashContext context = UnleashContext.builder()
            .userId(userId).build();
        if (unleash.isEnabled("useridfeature", context)) {
          	something = "<div style='padding:10px;'><div style='border-radius:5px; padding:10px; margin:5px; border:1px solid #e20074; display:inline-block;'><b>Enables the feature for a list of target users.</b> <p>It is implemented using the Unleash <a href='https://unleash.github.io/docs/activation_strategy#userwithid'>userwithid</a> activation strategy.</p></div></div>";
        } 
        else {
        	something = "";
        }
        return something;
    }
}