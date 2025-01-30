package com.naiduit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.naiduit.service.CustomerService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	@Autowired
	private CustomerService customerService;
	
	@Bean
	public BCryptPasswordEncoder pazzwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider authProvider = new 
				DaoAuthenticationProvider();
		
		authProvider.setPasswordEncoder(pazzwordEncoder());
		authProvider.setUserDetailsService(customerService);
		
		return authProvider;
		
	}
	
	@Bean
	@SneakyThrows
	public AuthenticationManager authManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}
	
		
	@Bean
	@SneakyThrows
	public SecurityFilterChain security(HttpSecurity http) {
		
		http.authorizeHttpRequests((req) -> {
			req.requestMatchers("/register")
			   .permitAll()
			   .anyRequest()
			   .authenticated();						
		});
		
		return http.csrf().disable().build();
		
	}

}
