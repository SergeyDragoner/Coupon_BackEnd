package com.example.demo.Beans;

import com.example.demo.Enums.ClientType;

public class Credentials {
    private String email, password;
    private ClientType type;

    public Credentials(String username, String password, ClientType type) {
        this.email = username;
        this.password = password;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }
}
