package com.naiduit.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naiduit.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
			Customer findByEmail(String email);
}
