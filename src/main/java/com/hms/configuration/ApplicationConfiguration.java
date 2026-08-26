package com.hms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hms.api.login.UserDetailsRepo;

@Configuration
public class ApplicationConfiguration {

	private final UserDetailsRepo repo;

	public ApplicationConfiguration(UserDetailsRepo repo) {
		this.repo = repo;
	}

	@Bean
	BCryptPasswordEncoder getPassWordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found in database"));
	}

	@Bean
	AuthenticationManager authenticatioManager(UserDetailsService userDetailsService, PasswordEncoder passEncoder) {
		DaoAuthenticationProvider prov = new DaoAuthenticationProvider(userDetailsService);
		prov.setPasswordEncoder(passEncoder);
		return new ProviderManager(prov);
	}

}
