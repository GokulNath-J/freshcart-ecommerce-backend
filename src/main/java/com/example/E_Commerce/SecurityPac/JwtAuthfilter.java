package com.example.E_Commerce.SecurityPac;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthfilter extends OncePerRequestFilter {


    private JwtClass jwtClass;

    private UserDetailServiceImpl userDetailService;

    public JwtAuthfilter(JwtClass jwtClass, UserDetailServiceImpl userDetailService) {
        this.jwtClass = jwtClass;
        this.userDetailService = userDetailService;
    }

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthfilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String username = null;
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtClass.extractUsername(token);
            logger.info("header!= null && header.startsWith(Bearer ) is true");
            //log.debug("header!= null && header.startsWith(Bearer ) is true");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            logger.info("username!=null && SecurityContextHolder.getContext().getAuthentication() == null (true)");

            if (jwtClass.verifyToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.
                        setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.info("Token Verified");
            }

        }

        filterChain.doFilter(request, response);
    }
}
