package com.merge_conflict.AuthModule.AuthJwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Secret key used to sign and validate the JWT tokens, encoded in Base64
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    // Method to generate a token using only the user details
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user); // No extra claims are provided
    }

    // Method to generate a token with additional claims and user details
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)  // Set extra claims (if any)
                .setSubject(user.getUsername())  // Set the subject of the token as the username
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the current date as the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))  // Set token expiration to 24 minutes
                .signWith(getKey(), SignatureAlgorithm.HS256)  // Sign the token using the secret key and HS256 algorithm
                .compact();  // Compact the token into its final string representation
    }

    // Helper method to decode the secret key and return a Key object
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);  // Decode the Base64-encoded secret key
        return Keys.hmacShaKeyFor(keyBytes);  // Generate an HMAC-SHA key from the byte array
    }

    // Method to extract the username from the token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);  // Use the getClaim method to extract the "subject" (username)
    }

    // Method to validate the token by checking if the username matches and if the token is not expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);  // Extract the username from the token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));  // Validate username and expiration
    }

    // Helper method to extract all claims from a token
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()  // Build a JWT parser
                .setSigningKey(getKey())  // Set the secret key for verifying the token signature
                .build()
                .parseClaimsJws(token)  // Parse the token and extract the claims
                .getBody();
    }

    // Generic method to extract a specific claim from the token
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);  // Get all claims from the token
        return claimsResolver.apply(claims);  // Apply the function to resolve the desired claim
    }

    // Helper method to extract the expiration date from the token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);  // Use getClaim to extract the expiration date
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());  // Compare the expiration date with the current date
    }
}
