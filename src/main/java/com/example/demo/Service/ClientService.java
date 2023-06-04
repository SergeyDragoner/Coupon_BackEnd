package com.example.demo.Service;

import com.example.demo.Exceptions.IncorrectCredentialsException;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.CouponRepository;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class ClientService {

    protected CustomerRepository customerRepo;

    protected CompanyRepository companyRepo;

    protected CouponRepository couponRepo;

    public ClientService(CustomerRepository customerRepo, CompanyRepository companyRepo, CouponRepository couponRepo) {
        this.customerRepo = customerRepo;
        this.companyRepo = companyRepo;
        this.couponRepo = couponRepo;
    }

    public ClientService() {}

    public abstract int login(String email, String password) throws IncorrectCredentialsException;
}