package com.springjwtsecurity.springjwtscurity.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "String";
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String,Object> claims, String subject) {
        return Jwts.builder().addClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 *60 *10)).
                signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }

    public String extractUserName(String token){
        return claimExtract(token, Claims::getSubject);
    }

    public Date extractExpiryDate(String token){
        return claimExtract(token, Claims::getExpiration);
    }

    private <T> T claimExtract(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        return extractExpiryDate(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String userName  = extractUserName(token);
        return userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
