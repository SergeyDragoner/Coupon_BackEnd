package com.example.demo.Service;

import com.example.demo.Beans.Coupon;
import com.example.demo.Beans.Customer;
import com.example.demo.Enums.Category;
import com.example.demo.Exceptions.*;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.CouponRepository;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
@Scope("prototype")
public class CustomerService extends ClientService{

    //while logging in customer id will be recognized with the specific customer
    private int customerID;

    public CustomerService(CustomerRepository customerRepo, CompanyRepository companyRepo, CouponRepository couponRepo) {
        super(customerRepo, companyRepo, couponRepo);
    }

    /**
     * This method allows access to the customer facade throw log in validation
     *
     * @param email
     * @param password
     * @throws IncorrectCredentialsException
     * @retur true if email and password are correct or false if it isn't found in the db
     */
    @Override
    public int login(String email, String password) throws IncorrectCredentialsException {
        Optional<Customer> customer = customerRepo.findByEmailAndPassword(email, password);
        if (customer.isPresent()) {
            customerID = customer.get().getId();
            System.out.println("Logged In");
            return customerID;
        }
        else
        System.out.println("Wrong Information For The Log In Buddy");
         return -1;
    }

    //CREATE

    /**checks if the coupon is not expired, if it's out of stock, if the customer hadn't already purchased it.
     * if not then gets the customer's object, pulls its coupons by the id, adds the coupon to the customer's coupon
     * collection, update the customer, substructure from the coupon amount and update the coupon.
     * @param coupon a full coupon object to be purchased
     * @throws CouponExpiresException can't purchase a coupon after it's end date
     * @throws CouponNotInStockException can't purchase a coupons if it's amount is lower than 1
     * @throws CouponAlreadyBoughtException can't purchase the same coupon twice
     * @throws NotFoundException can't find customer
     */
    @Transactional
    public void purchaseCoupon(Coupon coupon) throws CouponExpiresException, CouponNotInStockException,
            CouponAlreadyBoughtException, NotFoundException {
        if(Calendar.getInstance().getTime().after(coupon.getEndDate()))
            throw new CouponExpiresException("This Coupon Has Passed it EndDay, And Expires");
        if(coupon.getAmount() < 1)
            throw new CouponNotInStockException("There isn't stock Available From This Coupon");
        if(couponRepo.findCouponPurchase(customerID, coupon.getId()).isPresent())
            throw new CouponAlreadyBoughtException();
        //get an object of the current customer and its coupons
        Customer customer = customerRepo.findById(customerID).orElseThrow( () -> new NotFoundException("Customer not found"));
        List<Coupon> customerCoupons = customer.getCoupons();
        //add the coupon to the new coupon collection
        customerCoupons.add(coupon);
        //save the customer with the new collection to the database
        customer.setCoupons(customerCoupons);
        coupon.setAmount(coupon.getAmount()-1);
        customerRepo.save(customer);
        //update coupon amount
        //save coupon with updated amount to the database
        System.out.println("Customer " + customer.getFirstName() + " " + customer.getLastName() + " purchased " +
                coupon.getTitle() + " coupon");
    }


    //READ

    /** @return list of all coupons
     */
    public List<Coupon> getAllCoupons(){
        return couponRepo.findAll();
    }

    /**@param id the ID of the requested coupon
     * @return coupon by a given ID
     * @throws NotFoundException could not find coupon
     */
    public Coupon getCoupon(int id) throws NotFoundException {
        return couponRepo.findById(id).orElseThrow( () -> new NotFoundException("Coupon not found"));
    }

    /**@param id the ID of the requested coupon
     * @return coupon from the customer's collection by a given ID
     * @throws NotFoundException could not find coupon
     */
    public Coupon getCustomerCoupon(int id) throws NotFoundException {
        List<Coupon> customerCoupons = getCustomerCoupons();
        for(Coupon coupon : customerCoupons){
            if(coupon.getId() == id)
                return coupon;
        }
        throw new NotFoundException("Coupon not found");
    }

    /**@return list of the customer's purchased coupons
     */
    public List<Coupon> getCustomerCoupons(){
        List<Integer> customerCouponsId = couponRepo.findCustomerCouponsId(customerID);
        List<Coupon> customerCoupons = new ArrayList<>();
        customerCouponsId.forEach( id -> customerCoupons.add(couponRepo.findById(id).get()) );
        return customerCoupons;
    }

    /**@param category the category of the coupons you want to receive
     * @return list of coupons for the given category
     */
    public List<Coupon> getCustomerCoupons(Category category){
        List<Coupon> customerCoupons = getCustomerCoupons();
        List<Coupon> filteredCoupons = new ArrayList<>();
        customerCoupons.stream().filter( coupon -> couponRepo.findById(coupon.getId()).get().getCategory().equals(category) )
                .forEach( coupon -> filteredCoupons.add(couponRepo.findById(coupon.getId()).get()) );
        return filteredCoupons;
    }

    /**@param max maximum price
     * @return list of the customer's coupons under the given maximum price
     */
    public List<Coupon> getCustomerCoupons(double max) {
        List<Coupon> customerCoupons = getCustomerCoupons();
        List<Coupon> filteredCoupons = new ArrayList<>();
        customerCoupons.stream().filter( coupon -> (couponRepo.findById(coupon.getId()).get().getPrice() <= max) )
                .forEach( coupon -> filteredCoupons.add(couponRepo.findById(coupon.getId()).get()) );
        return filteredCoupons;
    }

    /** @return customer object with coupons
     */
    public Customer getCustomerDetail(){
        Customer customer = customerRepo.findById(customerID).get();
        customer.setCoupons(getCustomerCoupons());
        return customer;
    }
}
