package com.example.demo.Exceptions;

public class CouponPriceDoesntExistException extends Exception {
    public CouponPriceDoesntExistException(){
        super("Coupon Price Doesnt Exist");
    }
}
