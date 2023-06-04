package com.example.demo.Service;

import com.example.demo.Beans.Company;
import com.example.demo.Beans.Coupon;
import com.example.demo.Enums.Category;
import com.example.demo.Exceptions.AlreadyExistsException;
import com.example.demo.Exceptions.CouponException;
import com.example.demo.Exceptions.CouponExpiredException;
import com.example.demo.Exceptions.NotFoundException;
import com.example.demo.Repository.CompanyRepository;
import com.example.demo.Repository.CouponRepository;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Scope("prototype")
public class CompanyService extends ClientService {
    private int companyId;

    public CompanyService(CustomerRepository customerRepo, CompanyRepository companyRepo, CouponRepository couponRepo) {
        super(customerRepo, companyRepo, couponRepo);
    }

    @Override
    public int login(String email, String password) {
        Optional<Company> company = companyRepo.findByEmailAndPassword(email, password);
        if (company.isPresent()) {
            companyId = company.get().getId();
            return companyId;
        } else return -1;
    }

    //CREATE

    /**
     * checks if the coupon's companyId matches that of the company that adds it, then checks that the label
     * doesn't match that of one of the company's other coupons, and then adds the coupon to the database.
     *
     * @param coupon the coupon to be added
     * @throws AlreadyExistsException coupon already exists
     * @throws NotFoundException      company ID doesn't match
     */
    public void addCoupon(Coupon coupon) throws NotFoundException, AlreadyExistsException, CouponExpiredException {
        coupon.setCompanyId(companyId);
        List<Coupon> companyCoupons = couponRepo.findAllByCompanyId(companyId);
        //find if coupon already exists

        for (Coupon compCoup : companyCoupons) {
            if (compCoup.getTitle().equals(coupon.getTitle()))
                throw new AlreadyExistsException("Coupon already exists");
        }
        //find if endDate has passed
        if (coupon.getEndDate().toLocalDate().isBefore(LocalDate.now()))
            throw new CouponExpiredException("Can't create an expired coupon");
        //save coupon
        couponRepo.save(coupon);

    }

    //READ

    /**
     * @param id the ID of the requested coupon
     * @return one of the companies coupons by a given ID
     * @throws NotFoundException coupon not found
     */
    public Coupon getCompanyCoupon(int id) throws NotFoundException {
        List<Coupon> companyCoupons = getCompanyCoupons();
        for (Coupon companyCoupon : companyCoupons) {
            if (companyCoupon.getId() == id)
                return couponRepo.findById(id).orElseThrow(() -> new NotFoundException("Coupon not found"));
        }
        throw new NotFoundException("Coupon not found");
    }

    /**
     * @return list of all the company's coupons
     */
    public List<Coupon> getCompanyCoupons() {
        return couponRepo.findAllByCompanyId(companyId);
    }

    /**
     * @param category the category by which to filter
     * @return list of all the company's coupons of a given category
     */
    public List<Coupon> getCompanyCoupons(Category category) {
        return couponRepo.findByCompanyIdAndCategory(companyId, category);
    }

    /**
     * @param max the maximum price for coupons to be returned
     * @return list of all company's coupons under a given price tag
     */
    public List<Coupon> getCompanyCoupons(double max) {
        return couponRepo.findByPriceBetween(0, max);
    }

    /**
     * @return company object with coupons
     * @throws NotFoundException company not found
     */
    public Company getCompanyDetail() throws NotFoundException {
        Company company = companyRepo.findById(companyId).orElseThrow(() -> new NotFoundException("Company not found"));
        company.setCoupons(couponRepo.findAllByCompanyId(companyId));
        return company;
    }

    //UPDATE

    /**
     * Update an existing coupon of the company
     *
     * @param coupon Coupon to be updated
     * @throws CouponException - if the coupon ID or company ID is attempted to be changed, or the company ID of the coupon being updated is different from the company's ID
     */
    public void updateCoupon(int couponId, Coupon coupon) throws CouponException {
        // Can't update coupon ID & Company ID
        Optional<Coupon> couponOptional = couponRepo.findById(couponId);
        if (couponOptional.isPresent()) {
            Coupon couponFromDB = couponOptional.get();
            if (couponFromDB.getId() == coupon.getId()) {
                if (couponFromDB.getCompanyId() == companyId) {
                    coupon.setCompanyId(couponFromDB.getCompanyId());
                    couponRepo.save(coupon);
                    System.out.println("Coupon updated successfully!");
                } else {
                    throw new CouponException("Cannot update the coupon ID or company ID!");
                }
            } else {
                throw new CouponException("Coupon does not exist!");
            }
        }

    }

    //DELETE

    /**
     * checks if the coupon exists by comparing the id to the database, if found deletes the purchases of the coupon
     * then deletes it
     *
     * @param couponId the ID of the coupon
     * @throws NotFoundException could not find coupon
     */
    public void deleteCoupon(int couponId) throws NotFoundException {
        if (!couponRepo.existsById(couponId))
            throw new NotFoundException("Coupon not found");
        couponRepo.deletePurchasesByCouponId(couponId);
        couponRepo.deleteById(couponId);
        System.out.println("Coupon no' " + couponId + " was deleted");
    }
}
