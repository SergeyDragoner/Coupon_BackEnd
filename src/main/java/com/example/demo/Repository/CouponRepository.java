package com.example.demo.Repository;

import com.example.demo.Beans.Coupon;
import com.example.demo.Enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findAllByCompanyId(int id);

    List<Coupon> findByCompanyIdAndCategory(int companyId, Category category);

    List<Coupon> findByPriceBetween(double min, double max);

    @Query (value = "SELECT COUPONS_ID FROM CUSTOMERS_COUPONS WHERE CUSTOMERS_ID = :id", nativeQuery = true)
    List<Integer> findCustomerCouponsId(@Param(value = "id") int customerId);

    @Query(value = "SELECT CUSTOMERS_ID FROM CUSTOMERS_COUPONS WHERE CUSTOMERS_ID = :customerId AND COUPONS_ID = :couponId", nativeQuery = true)
    Optional<Integer> findCouponPurchase(@Param(value = "customerId") int customerId, @Param(value = "couponId") int couponId);

    @Modifying
    @Transactional
    @Query (value = "DELETE FROM CUSTOMERS_COUPONS WHERE COUPONS_ID = :id", nativeQuery = true)
    void deletePurchasesByCouponId(@Param(value = "id") int id);

    @Modifying
    @Transactional
    @Query (value = "DELETE FROM CUSTOMERS_COUPONS WHERE CUSTOMERS_ID = :id", nativeQuery = true)
    void deletePurchasesByCustomerId(@Param(value = "id") int id);

    @Modifying
    @Transactional
    @Query (value = "DELETE FROM COUPONS WHERE COMPANY_ID = :id", nativeQuery = true)
    void deleteCouponByCompanyId(@Param(value = "id") int companyId);
}