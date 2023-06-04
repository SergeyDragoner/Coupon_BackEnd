package com.example.demo.Repository;

import com.example.demo.Beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {
    boolean existsByName(String name);

    boolean existsByEmail(String email);

    Optional<Company> findByEmailAndPassword(String email, String password);
}
