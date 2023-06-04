package com.example.demo.Controllers;

import com.example.demo.Beans.Company;
import com.example.demo.Beans.Customer;
import com.example.demo.Beans.Session;
import com.example.demo.Exceptions.AlreadyExistsException;
import com.example.demo.Exceptions.CompanyExistsException;
import com.example.demo.Exceptions.CustomerExistsException;
import com.example.demo.Exceptions.NotFoundException;
import com.example.demo.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private Map<String, Session> sessionPack;
    @Autowired
    private HttpServletRequest request;


    public AdminService getService() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Session session = sessionPack.get(token);
        session.setLastUse(System.currentTimeMillis());
        return (AdminService) session.getService();
    }

    @PostMapping(path = "/company")
    public ResponseEntity<?> addCompany(@RequestBody Company company) throws AlreadyExistsException, CompanyExistsException {
        return ResponseEntity.ok(getService().addCompany(company));
    }

    @PostMapping(path = "/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        try{
            return ResponseEntity.ok(getService().addCustomer(customer));
        } catch (CustomerExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "/companies")
    public ResponseEntity<?> getAllCompanies(){
        try{
            return ResponseEntity.ok(getService().getAllCompanies());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Sorry, something went wrong, please try again");
        }
    }

    @GetMapping(path = "/company/{id}")
    public ResponseEntity<?> getOneCompany(@PathVariable int id){
        try {
            return ResponseEntity.ok(getService().getOneCompany(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
          return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @GetMapping(path = "/customers")
    public ResponseEntity<?> getAllCustomers(){
        try {
            return ResponseEntity.ok(getService().getAllCustomers());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Sorry, something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<?> getOneCustomer(@PathVariable int id){
        try {
            return ResponseEntity.ok(getService().getOneCustomer(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @PutMapping(path = "/company")
    public ResponseEntity<?> updateCompany(@RequestBody Company company){
        try {
            return ResponseEntity.ok(getService().updateCompany(company));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again later");
        }
    }

    @PutMapping(path = "/customer")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer){
        try {
            return ResponseEntity.ok(getService().updateCustomer(customer));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @DeleteMapping(path = "/company/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable int id){
        try {
            getService().deleteCompany(id);
            return ResponseEntity.ok("Company deleted");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @DeleteMapping(path = "/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id){
        try {
            getService().deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }
}