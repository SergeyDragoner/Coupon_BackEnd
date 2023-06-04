package com.example.demo.Exceptions;

public class CouponCategoryDoesntExist extends Exception {
    public CouponCategoryDoesntExist(String this_coupon_Categroy_isnt_available) {
        super("This Coupon Category Isn't Available");
    }
}


