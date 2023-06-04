package com.example.demo.Repository;

import com.example.demo.Beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
  boolean existsByEmail(String email);
 Optional<Customer> findByEmailAndPassword(String email, String password);
}
