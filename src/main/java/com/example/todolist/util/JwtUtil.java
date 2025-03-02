package com.example.todolist.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

//file ini nyediain method dari jwt

@Component
public class JwtUtil {
    //secret key : fungsinya untuk kunci akses ke backend
    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



    public String generateToken(String username, boolean isAdmin) {
        return Jwts.builder()
                .setSubject(username)  // Subject is the username
                .claim("role", isAdmin ? "ROLE_ADMIN" : "ROLE_USER")  // Ensure it adds "ROLE_" prefix for roles
                .claim("isAdmin", isAdmin)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 hours
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    //validasi token dan user yang dikirim

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Compare the extracted username with the username in UserDetails
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
