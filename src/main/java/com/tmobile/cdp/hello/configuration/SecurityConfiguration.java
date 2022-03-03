package com.tmobile.cdp.hello.configuration;

import com.tmobile.cdp.hello.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author Sony K Sunny
 *
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * Configures the endpoints which are non-secured (don't need any security).
	 */
	@Override
	public void configure(WebSecurity webSecurity) {
		webSecurity.ignoring()
                        .antMatchers(HttpMethod.GET, "/actuator/health/liveness")
                        .antMatchers(HttpMethod.GET, "/actuator/health/readiness")
                        .antMatchers(HttpMethod.GET, "/actuator/health");
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }
}
