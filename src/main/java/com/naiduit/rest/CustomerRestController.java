package com.naiduit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naiduit.entity.Customer;
import com.naiduit.service.CustomerService;

@RestController
public class CustomerRestController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Customer c){
				
		UsernamePasswordAuthenticationToken token =  
				new UsernamePasswordAuthenticationToken(c.getEmail(), c.getPazzword());
				
			// verify login details valid or not
			Authentication authenticate = authManager.authenticate(token);
			
			boolean status = authenticate.isAuthenticated();
					
			if(status)
			{ 
				// Generate JWT and send to client
				return new ResponseEntity<String>("Welcome", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("Failed", HttpStatus.BAD_REQUEST);
			}
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody Customer c){
		boolean status = customerService.saveCustomer(c);
		if(status)
		{ 
			return new ResponseEntity<>("Success", HttpStatus.CREATED);
		}
		else  {
			return new ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
