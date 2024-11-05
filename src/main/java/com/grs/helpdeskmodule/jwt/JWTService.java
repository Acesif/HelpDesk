package com.grs.helpdeskmodule.jwt;

import com.grs.helpdeskmodule.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {

    @Value("${jwt.secret}")
    String JWT_SECRET;
    public String generateToken(String username, User user){

        Map<String,Object> claims = new HashMap<>();
        claims.put("name",user.getName());
        claims.put("email",user.getEmail());
        claims.put("phone_number",user.getPhoneNumber());
        claims.put("office_id",user.getOfficeId());
        claims.put("designation",user.getDesignation());

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigninKey())
                .compact();
    }

    public SecretKey getSigninKey()  {

        SecretKey key = null;

        try {
            byte[] keyBytes = Base64.getDecoder().decode(JWT_SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e){
            log.error(Arrays.toString(e.getStackTrace()));;
        }
        return key;
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(java.lang.String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractEmail(token);
        return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
