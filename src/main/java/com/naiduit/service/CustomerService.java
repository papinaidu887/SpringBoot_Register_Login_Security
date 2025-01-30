package com.naiduit.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.naiduit.entity.Customer;
import com.naiduit.repo.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService {
	
	@Autowired
	private BCryptPasswordEncoder pazzwordEncoder;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Customer c = customerRepo.findByEmail(email);
		return new User(c.getEmail(), c.getPazzword(), Collections.emptyList());
	
	}
	
	public boolean saveCustomer(Customer c) {
		
		String encodePazzword = pazzwordEncoder.encode(c.getPazzword());
		c.setPazzword(encodePazzword);
		
		Customer savedCustomer = customerRepo.save(c);
		
		return savedCustomer.getCid()!=null;
	}

}
