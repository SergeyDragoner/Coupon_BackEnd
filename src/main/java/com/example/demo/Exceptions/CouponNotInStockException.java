package com.example.demo.Exceptions;

public class CouponNotInStockException  extends Exception{
    public CouponNotInStockException(String this_coupon_isnt_available)
    {
        super("There isn't stock Available From This Coupon");
    }
}