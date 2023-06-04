package com.example.demo.Exceptions;

public class CouponExpiredException extends Exception {
    public CouponExpiredException() {
        super("Coupon has already expired");
    }
    public CouponExpiredException(String message){
        super(message);
    }
}
