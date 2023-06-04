package com.example.demo;
import com.example.demo.Beans.Session;
import com.example.demo.Exceptions.*;
import com.example.demo.Service.Login.LoginManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CouponProjectApplication {


    public static void main(String[] args) throws IncorrectCredentialsException, NotFoundException {
        ConfigurableApplicationContext ctx = SpringApplication.run(CouponProjectApplication.class, args);
            Thread CouponExpirationDailyJob = new Thread(ctx.getBean(com.example.demo.Threads.CouponExpirationDailyJob.class));
            CouponExpirationDailyJob.start();
        LoginManager login = ctx.getBean(LoginManager.class);
//        try {
//            CompanyService comp = (CompanyService)login.Login("A@A", "1234", ClientType.COMPANY);
//            Coupon coupon = new Coupon(1,123, Category.BEVERAGES,"good",new Date(2023,1,12), new Date(2023,12,12), 123);
//            comp.addCoupon(coupon);
//        } catch (IncorrectCredentialsException | NotFoundException | LoginErrorException | AlreadyExistsException |
//                 CouponExpiredException e) {
//            System.out.println(e.getMessage());
//





        }

    @Bean
    public Map<String, Session> sessionPack(){
        return new HashMap<String, Session>();
    }


    }


