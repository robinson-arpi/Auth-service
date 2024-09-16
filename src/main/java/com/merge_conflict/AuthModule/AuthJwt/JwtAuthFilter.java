package com.merge_conflict.AuthModule.AuthJwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Dependency injection for JWT service and user details service
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // This method is called once per request to filter the authentication process
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract token from the request's Authorization header
        final String token = getTokenFromRequest(request);
        final String username;

        // If no token is present, continue the filter chain without doing anything
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the token using the JwtService
        username = jwtService.getUsernameFromToken(token);

        // If username is not null and no authentication is already set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user details using the username extracted from the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Check if the token is valid for the user details
            if (jwtService.isTokenValid(token, userDetails)) {

                // Create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No credentials needed, as JWT is already verified
                        userDetails.getAuthorities());

                // Set additional details from the request (e.g., IP address, session id)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authenticated user in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        // Continue with the filter chain, passing the request and response to the next filter
        filterChain.doFilter(request, response);
    }

    // Helper method to extract the JWT token from the Authorization header in the request
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header has text and starts with "Bearer "
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Return the token part of the header (after "Bearer ")
            return authHeader.substring(7);
        }
        // If no valid token is found, return null
        return null;
    }
}