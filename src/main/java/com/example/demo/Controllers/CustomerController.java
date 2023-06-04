package com.example.demo.Controllers;

import com.example.demo.Beans.Coupon;
import com.example.demo.Beans.Session;
import com.example.demo.Enums.Category;
import com.example.demo.Exceptions.*;
import com.example.demo.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private Map<String, Session> sessionPack;
    @Autowired
    private HttpServletRequest request;

    public CustomerService getService() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Session session = sessionPack.get(token);
        session.setLastUse(System.currentTimeMillis());
        return (CustomerService) session.getService();
    }

    @PostMapping
    public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon){
            try {
                getService().purchaseCoupon(coupon);
                return ResponseEntity.ok("Coupon Purchased");
            } catch (CouponExpiresException | CouponNotInStockException | NotFoundException |
                     CouponAlreadyBoughtException e) {
             return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
            }
    }

    @GetMapping(path = "/coupons")
    public ResponseEntity<?> getAllCoupons (){
        return ResponseEntity.ok(getService().getAllCoupons());
    }

    @GetMapping(path = "/customersCoupons")
    public ResponseEntity<?> getCustomerCoupons(){
        try {
            return ResponseEntity.ok(getService().getCustomerCoupons());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/couponsCategory/{category}")
    public ResponseEntity<?> getCustomerCoupons(@PathVariable Category category){
        try {
            return ResponseEntity.ok(getService().getCustomerCoupons(category));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/coupons/{max}")
    public ResponseEntity<?> getCustomerCoupons(@PathVariable double max){
        try {
            return ResponseEntity.ok(getService().getCustomerCoupons(max));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/details")
    public ResponseEntity<?> getCustomerDetails(){
        try {
            return ResponseEntity.ok(getService().getCustomerDetail());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }
}
