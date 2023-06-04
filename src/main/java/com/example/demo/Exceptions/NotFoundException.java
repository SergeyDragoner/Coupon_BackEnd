package com.example.demo.Exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(String error){
        super(error);
    }
}