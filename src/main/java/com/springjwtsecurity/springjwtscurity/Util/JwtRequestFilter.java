package com.springjwtsecurity.springjwtscurity.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Intercept the any request and examine the token
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String autherazitionHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        //These lines retrieve the "Authorization" header from the incoming HTTP request. If the header exists and starts with "Bearer ", it's assumed to be a JWT token
        if(autherazitionHeader != null && autherazitionHeader.startsWith("Bearer ")){
            jwt = autherazitionHeader.substring(7);
            username = jwtUtil.extractUserName(jwt);
        }

        //These lines check if the username is not null (meaning a valid username was extracted from the token) and if the current authentication context (whether the user is already authenticated) is null.
        //If both conditions are met, the user details are loaded from the UserDetailsService. Then, the JWT token is validated using the JwtUtil.
        //If the token is valid, a new UsernamePasswordAuthenticationToken is created, which represents the authenticated user. The token is set to contain the user details and authorities.
        //The authentication token is associated with the current security context using SecurityContextHolder.getContext().setAuthentication().
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //this line continues the request processing by passing it along the filter chain. It allows the request to proceed to the next filters or the application's endpoint.
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
