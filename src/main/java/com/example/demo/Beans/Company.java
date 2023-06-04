package com.example.demo.Beans;

import javax.persistence.*;
import java.util.List;


@Entity
@Table (name = "Companies")
public class Company extends Client {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String name, email, password;
    @OneToMany (mappedBy = "companyId", fetch = FetchType.LAZY)
    private List<Coupon> coupons;

    public Company() {
    }

    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //GETTERS
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

//SETTERS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    //METHODS

    @Override
    public String toString() {
        return "Company no' " + id + " - " + name + ", email: " + email +'\n';
    }
}
