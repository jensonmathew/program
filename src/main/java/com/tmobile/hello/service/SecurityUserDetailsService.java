package com.tmobile.hello.service;

import java.util.Optional;

import com.tmobile.hello.domain.SecurityUserDetails;
import com.tmobile.hello.repository.FeatureflagsRepository;
import com.tmobile.hello.repository.model.Featureflags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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
			return userOpt.map(SecurityUserDetails::new).get();
		} else {
			throw new UsernameNotFoundException("Cannot find a user with user name '" + username + "'!");
		}
	}

}
