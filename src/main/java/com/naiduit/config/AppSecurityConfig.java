package com.naiduit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.naiduit.filter.AppFilter;
import com.naiduit.service.CustomerService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	@Autowired
	private AppFilter filter;
	
	@Autowired
	private CustomerService customerService;
	
	@Bean
	public PasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider authProvider = new 
				DaoAuthenticationProvider();
		
	
		authProvider.setUserDetailsService(customerService);
		authProvider.setPasswordEncoder(pwdEncoder());
		
		return authProvider;
		
	}
	
	@Bean
	@SneakyThrows
	public AuthenticationManager authManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}
	
	@Bean
	@SneakyThrows
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		
		  return http.csrf().disable()
	                .authorizeHttpRequests()
	                .requestMatchers("/api/login","/api/register").permitAll()
	                .and()
	                .authorizeHttpRequests().requestMatchers("/api/**")
	                .authenticated()
	                .and()
	                .sessionManagement()
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	                .and()
	                .authenticationProvider(authProvider())
	                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
		
		/*http.authorizeHttpRequests((req) -> {
			req.requestMatchers("/register", "/login")
			   .permitAll()
			   .anyRequest()
			   .authenticated();						
		});
		
		return http.csrf().disable().build();*/
		
	}

}
