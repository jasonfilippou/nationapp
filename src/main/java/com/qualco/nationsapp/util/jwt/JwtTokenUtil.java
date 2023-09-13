package com.qualco.nationsapp.util.jwt;



import com.qualco.nationsapp.controller.JwtAuthenticationController;
import com.qualco.nationsapp.service.jwt.JwtAuthenticationService;
import com.qualco.nationsapp.service.jwt.JwtUserDetailsService;
import com.qualco.nationsapp.util.logger.Logged;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.qualco.nationsapp.util.Constants.JWT_VALIDITY;

/**
 * Various utilities for generating and parsing JWTs.
 *
 * @author jason
 * @see JwtAuthenticationController
 * @see JwtAuthenticationService
 */
@Component
@Logged
public class JwtTokenUtil implements Serializable {
    @Serial private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retrieve the user's username from the provided token.
     *
     * @param token The JWT to retrieve the username from.
     * @return The user's username.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve the expiration date of the token.
     *
     * @param token The token to retrieve the expiration date from.
     * @return A {@link LocalDateTime} instance representing the moment in time that the token will
     *     expire.
     */
    public LocalDateTime getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    /**
     * Generate the token for the provided user.
     *
     * @param userDetails The {@link UserDetails} of the provided user.
     * @return The generated JWT.
     * @see JwtUserDetailsService
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // while creating the token -
    // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Validate the provided token against the details of the provided user.
     *
     * @param token A JWT.
     * @param userDetails The {@link UserDetails} to validate the token against.
     * @return {@literal true} if the token is validated, {@literal false} otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
