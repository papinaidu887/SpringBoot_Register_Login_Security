package com.naiduit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naiduit.entity.Customer;
import com.naiduit.repo.CustomerRepository;
import com.naiduit.service.JwtService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwt;
	
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome to Naidu";
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody Customer c){
				
		UsernamePasswordAuthenticationToken token =  
				new UsernamePasswordAuthenticationToken(c.getEmail(), c.getPazzword());
				
			
				try 
				{
					// verify login details valid or not
					Authentication authenticate = authManager.authenticate(token);
				
					if (authenticate.isAuthenticated()) {
						String jwtToken = jwt.generateToken(c.getEmail());
						return new ResponseEntity<>(jwtToken, HttpStatus.OK);
					}
				
				} 
				catch (Exception e) {
					//logger
				}
				return new ResponseEntity<String>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/register")
	public String registerCustomer(@RequestBody Customer customer){
		
				// duplicate check

				String encodedPwd = pwdEncoder.encode(customer.getPazzword());
				customer.setPazzword(encodedPwd);

				customerRepo.save(customer);

				return "User registered successfully";
		
		/*
		 * boolean status = customerService.saveCustomer(c); if(status) { return new
		 * ResponseEntity<>("Success", HttpStatus.CREATED); } else { return new
		 * ResponseEntity<>("Failed", HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
	}

}
