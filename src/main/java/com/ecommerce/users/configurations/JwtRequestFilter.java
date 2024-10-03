package com.ecommerce.users.configurations;

import com.ecommerce.users.services.JwtService;
import com.ecommerce.users.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/users/authenticate") ||
                request.getRequestURI().startsWith("/api/files/upload") ||
                request.getRequestURI().startsWith("/api/users/add") ||
                request.getRequestURI().startsWith("/api/products/add");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        Long userId = null;
        if (cookies == null){
            System.out.println("No cookies found");
            filterChain.doFilter(request, response);
            return;
        }System.out.println("cookies found"+cookies.toString());

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT")) {
                jwtToken = cookie.getValue();
                break;
            }
        }
        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userId = jwtService.extractUserId(jwtToken);
            UserDetails userDetails = userService.loadUserByUserId(userId);
            if (jwtService.validateToken(jwtToken, userId)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
