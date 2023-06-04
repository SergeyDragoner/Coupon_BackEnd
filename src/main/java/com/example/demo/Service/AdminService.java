package com.example.demo.Service;

import com.example.demo.Beans.Company;
import com.example.demo.Beans.Coupon;
import com.example.demo.Beans.Customer;
import com.example.demo.Exceptions.CustomerExistsException;
import com.example.demo.Exceptions.NotFoundException;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Exceptions.CompanyExistsException;
import com.example.demo.Repository.CouponRepository;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
    @Service
    public class AdminService extends ClientService {


        public AdminService(CustomerRepository customerRepo, CompanyRepository companyRepo, CouponRepository couponRepo) {
            super(customerRepo, companyRepo, couponRepo);
        }

        @Override
        public int login(String email, String password){
            if(email.equals("admin@admin.com") && password.equals("admin")) {
                System.out.println("Logged In");
                return 1;
            }
            else
                System.out.println("Wrong Information For The Log In Buddy");
                return -1;
        }
        //CREATE

        /**check if there's already a company with this name or email, if not add this to the database
         * @param company the company to be added
         * @throws CompanyExistsException company already exists
         */
        public Company addCompany (Company company) throws CompanyExistsException {
            if (companyRepo.existsByName(company.getName()) || companyRepo.existsByEmail(company.getEmail()))
                throw new CompanyExistsException("Sorry, name or email already taken");
             companyRepo.save(company);
            System.out.println("Company Added Successfully");
            return company;
        }

        /**check if there's already a customer with this email, if not add this to the database
         * @param customer the customer to be added
         * @throws CustomerExistsException customer already exists
         */
        public Customer addCustomer(Customer customer) throws CustomerExistsException {
            if(customerRepo.existsByEmail(customer.getEmail()))
                throw new CustomerExistsException("Sorry, email already taken");
            System.out.println("Customer Added Successfully");
            return customerRepo.save(customer);
        }

        //READ

        /**@return List all companies from the database
         */
        public List<Company> getAllCompanies(){
            return companyRepo.findAll();
        }

        /**@param id ID of the requested company
         * @return one company from the database by id
         * @throws NotFoundException company ID not found
         */
        public Company getOneCompany(int id) throws NotFoundException {
            return companyRepo.findById(id).orElseThrow( () -> new NotFoundException("Company id not found") );
        }

        /** @return list of all customers from the database
         */
        public List<Customer> getAllCustomers(){
            return customerRepo.findAll();
        }

        /**@param id ID of the requested customer
         * @return one customer from the database by id
         * @throws NotFoundException customer ID not found
         */
        public Customer getOneCustomer(int id) throws NotFoundException {
            Customer customer = customerRepo.findById(id).orElseThrow(()-> new NotFoundException("Customer id not found"));
            List<Integer> customerCouponsId = couponRepo.findCustomerCouponsId(id);
            List<Coupon> customerCoupons = new ArrayList<>();
            customerCouponsId.forEach( couponId -> customerCoupons.add(couponRepo.findById(id).get()) );
            customer.setCoupons(customerCoupons);
            return customer;
        }

        /** @return list of all coupons
         */
        public List<Coupon> getAllCoupons(){
            return couponRepo.findAll();
        }

        //UPDATE

        /** checks if the company exists by comparing id to the database, if it does then updates it in the database.
         * only updates email or password, not id, name or coupons
         * @param company the company to be updated
         * @throws NotFoundException company not found
         * @return updated company
         */
        public Company updateCompany(Company company) throws NotFoundException {
            if(companyRepo.existsById(company.getId())) {
                companyRepo.save(company);
                System.out.println("Company No:' " + company.getId() + " was updated");
                return companyRepo.findById(company.getId()).get();
            }
            else throw new NotFoundException("Company not found");
        }

        /** checks if the customer exists by comparing id to the database, if it does then updates it in the database.
         * only updates email, name or password, not id, or coupons
         * @param customer the customer to be added
         * @throws NotFoundException customer not found
         * @return updated customer
         */
        public Customer updateCustomer(Customer customer) throws NotFoundException {
            if(customerRepo.existsById(customer.getId())) {
                customerRepo.save(customer);
                System.out.println("Customer no' " + customer.getId() + " was updated");
                return customerRepo.findById(customer.getId()).get();
            }
            else throw new NotFoundException("Customer not found");
        }

        //DELETE

        /** checks if the company exists by comparing the company's id to the database, if found first deletes the
         * purchases of the company's coupons, then deletes the company's coupons and then deletes the company
         * @param id the ID of the company to be deleted
         * @throws NotFoundException company not found
         */
        public void deleteCompany(int id) throws NotFoundException {
            if(companyRepo.existsById(id)) {
                List<Coupon> companyCoupons = couponRepo.findAllByCompanyId(id);
                for(Coupon coupon : companyCoupons)
                    couponRepo.deletePurchasesByCouponId(coupon.getId());
                couponRepo.deleteCouponByCompanyId(id);
                companyRepo.deleteById(id);
                System.out.println("Company no' " + id + " was deleted");
            } else throw new NotFoundException("Company not found");
        }

        /**checks if the customer exists by comparing the customer's id to the database, if found first deletes the
         * customer's coupon purchases and then deletes the customer
         * @param id ID of the customer to be deleted
         * @throws NotFoundException customer not found
         */
        public void deleteCustomer(int id) throws NotFoundException {
            if(customerRepo.existsById(id)){
                couponRepo.deletePurchasesByCustomerId(id);
                customerRepo.deleteById(id);
                System.out.println("Customer no' " + id + " was deleted");
            } else throw new NotFoundException("Customer not found");
        }
    }
