package com.example.demo.Controllers;

import com.example.demo.Beans.Coupon;
import com.example.demo.Beans.Session;
import com.example.demo.Enums.Category;
import com.example.demo.Exceptions.AlreadyExistsException;
import com.example.demo.Exceptions.CouponException;
import com.example.demo.Exceptions.CouponExpiredException;
import com.example.demo.Exceptions.NotFoundException;
import com.example.demo.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@RestController
@RequestMapping(path = "/company")
public class CompanyController {

    @Autowired
    private Map<String, Session> sessionPack;
    @Autowired
    private HttpServletRequest request;



    public CompanyService getService() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Session session = sessionPack.get(token);
        session.setLastUse(System.currentTimeMillis());
        return (CompanyService) session.getService();
    }


    @PostMapping("/coupons/add")
    public ResponseEntity<?> addCoupon (@RequestBody Coupon coupon){
        try {
            getService().addCoupon(coupon);
            return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
        } catch (AlreadyExistsException | CouponExpiredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/coupons")
    public ResponseEntity<?> getCompanyCoupons(){
        try {
            return ResponseEntity.ok(getService().getCompanyCoupons());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/couponsCategory/{category}")
    public ResponseEntity<?> getCompanyCouponsByCategory(@PathVariable Category category){
        try {
            return ResponseEntity.ok(getService().getCompanyCoupons(category));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @GetMapping(path = "/coupons/{max}")
    public ResponseEntity<?> getCompanyCouponsByMaxPrice(@PathVariable double max){
        try {
            return ResponseEntity.ok(getService().getCompanyCoupons(max));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

    @GetMapping(path = "/details")
    public ResponseEntity<?> getCompanyDetails(){
        try{
            return ResponseEntity.ok(getService().getCompanyDetail());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again later");
        }
    }

        @PutMapping("/updateCoupon/{couponId}")
    public ResponseEntity<?> updateCoupon(@PathVariable int couponId, @RequestBody Coupon coupon){
        try {
            getService().updateCoupon(couponId, coupon);
            return ResponseEntity.ok("Updated coupon");
        } catch (CouponException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable int id){
        try {
            getService().deleteCoupon(id);
            return ResponseEntity.ok("Coupon deleted");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sorry, something went wrong, please try again");
        }
    }
}

