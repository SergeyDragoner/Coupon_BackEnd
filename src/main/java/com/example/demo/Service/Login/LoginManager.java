package com.example.demo.Service.Login;

import com.example.demo.Enums.ClientType;
import com.example.demo.Exceptions.IncorrectCredentialsException;
import com.example.demo.Exceptions.LoginErrorException;
import com.example.demo.Exceptions.NotFoundException;
import com.example.demo.Service.AdminService;
import com.example.demo.Service.ClientService;
import com.example.demo.Service.CompanyService;
import com.example.demo.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
@Service
public class LoginManager {

    private ApplicationContext ctx;

    public LoginManager(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**checks client type and tries to log in with it and given email and password
     * @param email email of the requested user
     * @param password password of the requested user
     * @param clientType client type of the requested user (Administrator/Company/CUSTOMER)
     * @return a service of the requested Administrator/Company/CUSTOMER
     * @throws IncorrectCredentialsException incorrect email or password
     * @throws NotFoundException unknown client type
     */
    public ClientService Login(String email, String password, ClientType clientType)
            throws IncorrectCredentialsException, NotFoundException, LoginErrorException {
        switch (clientType) {
            case ADMIN -> {
                AdminService adminService = ctx.getBean(AdminService.class);
                if (email.equals("admin@admin.com") && password.equals("admin")) {
                    return adminService;
                } else
                    throw new LoginErrorException("Your email or password isn't valid");
            }
            case COMPANY -> {
                CompanyService companyService = ctx.getBean(CompanyService.class);
                if (companyService.login(email, password) > 0) {
                    return companyService;
                } else throw new IncorrectCredentialsException();
            }
            case CUSTOMER -> {
                CustomerService customerService = ctx.getBean(CustomerService.class);
                if (customerService.login(email, password) > 0) {
                    return customerService;
                } else throw new IncorrectCredentialsException();
            }
        }
        throw new NotFoundException("Unknown Client Type");
    }
}