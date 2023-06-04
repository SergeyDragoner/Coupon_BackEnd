package com.example.demo.Filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.demo.Beans.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private Map<String, Session> sessionPack;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = request.getHeader("Authorization").replace("Bearer ", "");
            Session session = sessionPack.get(token);
            if (session != null) {

                String type = JWT.decode(token).getClaim("type").asString();
                switch (type) {
                    case "COMPANY":
                        int companyId = JWT.decode(token).getClaim("id").asInt();
                        String companyName = JWT.decode(token).getClaim("name").asString();
                        String companyEmail = JWT.decode(token).getClaim("email").asString();
                        break;

                    case "CUSTOMER":
                        int customerId = JWT.decode(token).getClaim("id").asInt();
                        String customerFirstName = JWT.decode(token).getClaim("firstName").asString();
                        String customerLastName = JWT.decode(token).getClaim("lastName").asString();
                        String customerEmail = JWT.decode(token).getClaim("email").asString();
                        break;
                }
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(401);
                response.getWriter().println("Please log in again");
            }
        } catch (JWTDecodeException JWTE){
            response.setStatus(401);
            response.getWriter().println("Invalid credentials!");
        }catch (Exception e){
            response.setStatus(500);
            response.getWriter().println("Oops, something went wrong, please try again later");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth");
    }
}
