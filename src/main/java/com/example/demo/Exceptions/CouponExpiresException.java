package com.example.demo.Exceptions;

public class CouponExpiresException extends Exception {
    public CouponExpiresException(String this_coupon_has_expires) {
        super("This Coupon Has Passed it EndDay, And Expires");
    }
}
