package com.example.demo.Threads;

import com.example.demo.Beans.Coupon;
import com.example.demo.Repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
@Service
public class CouponExpirationDailyJob implements Runnable {
    @Autowired
    private CouponRepository couponRepo;
    public boolean quit = false;

    /**gets a collection of all coupons from the database, filters all the ones who are not expired by the endDate
     * and deletes the remaining.
     * runs once a day between 00:00-00:30. to stop the Daily Job turn parameter 'quit' to true.
     */
    @Override
    public void run() {
        SimpleDateFormat spf = new SimpleDateFormat("HH");
        while (true){
            Calendar now = Calendar.getInstance();
            if (!spf.format(now.getTime().getTime()).equals("00")) {
                try {Thread.sleep(1000*60*30);} catch (InterruptedException ignore) {}
            }
            else break;
        }
        while(!quit){
            List<Coupon> coupons = couponRepo.findAll();
            List<Coupon> couponsToRemove = coupons.stream().filter(coupon -> coupon.getEndDate().before(Calendar.getInstance().getTime())).toList();

            for (Coupon couponToRemove : couponsToRemove) {
                couponRepo.deletePurchasesByCouponId(couponToRemove.getId());
                couponRepo.deleteById(couponToRemove.getId());
            }
            try { Thread.sleep(1000 * 60 * 60 * 24);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
