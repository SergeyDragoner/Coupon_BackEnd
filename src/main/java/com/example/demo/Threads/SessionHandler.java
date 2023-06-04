package com.example.demo.Threads;

import com.example.demo.Beans.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SessionHandler implements Runnable {
    @Autowired
    private Map<String, Session> sessionPack;

    public boolean quit = false;

    @Override
    public void run() {
        while(!quit){
            long now = System.currentTimeMillis();
            sessionPack.values().removeIf(session -> now - session.getLastUse() > 1000*60*30);
            System.out.println(sessionPack.values());
            try { Thread.sleep(1000*60);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
