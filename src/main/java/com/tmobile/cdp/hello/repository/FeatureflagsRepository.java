package com.tmobile.cdp.hello.repository;

import java.util.Optional;

import com.tmobile.cdp.hello.repository.model.Featureflags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureflagsRepository extends JpaRepository<Featureflags, Integer>{
	
	/**
	 * Function for finding the user by user name
	 * @param userName
	 * @return
	 */
	Optional<Featureflags> findByUser(String user);
	
	/**
	 * Findby user name and password.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the optional
	 */
	Optional<Featureflags> findByUserAndPassword(String user, String password);
}
