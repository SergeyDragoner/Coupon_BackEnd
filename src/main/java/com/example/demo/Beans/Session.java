package com.example.demo.Beans;

import com.example.demo.Service.ClientService;

public class Session {
    private String token;
    private final ClientService service;
    private long lastUse;

    public Session(ClientService service, String token) {
        this.token = token;
        this.service = service;
        this.lastUse = System.currentTimeMillis();
    }

    public ClientService getService() {
        return service;
    }

    public long getLastUse() {
        return lastUse;
    }

    public void setLastUse(long lastUse) {
        this.lastUse = lastUse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
