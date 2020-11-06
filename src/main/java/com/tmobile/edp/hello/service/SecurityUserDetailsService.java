package com.tmobile.edp.hello.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tmobile.edp.hello.domain.SecurityUserDetails;
import com.tmobile.edp.hello.repository.FeatureflagsRepository;
import com.tmobile.edp.hello.repository.model.Featureflags;


/**
 * @author Sony K Sunny
 *
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

	@Autowired
	private FeatureflagsRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Featureflags> userOpt = userRepository.findByUser(username);
		if (userOpt.isPresent()) {
			SecurityUserDetails securityUserDetails = userOpt.map(SecurityUserDetails::new).get();
			return securityUserDetails;
		} else {
			throw new UsernameNotFoundException("Cannot find a user with user name '" + username + "'!");
		}
	}

}
