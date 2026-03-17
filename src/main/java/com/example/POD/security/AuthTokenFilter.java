package com.example.POD.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String bypassHeader = request.getHeader("X-Bypass-DB");
            String jwt = parseJwt(request);
            // Check karein ki jwt string "null" ya "undefined" na ho (common frontend bug)
            if (jwt != null && !jwt.equalsIgnoreCase("null") && !jwt.equalsIgnoreCase("undefined") && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);

                //for exam portal user which not stored in this backend database
                if ("true".equals(bypassHeader)) {

                    // Create a manual UserDetails object (No DB call)
                    UserDetails userDetails = User.withUsername(email)
                            .password("")
                            .authorities("ROLE_STUDENT")
                            .build();

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                //for user whose data stored in this backend database
                else{
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            else{
                //incase of bearer token missing or malformed
                sendErrorResponse(response, "Token is missing or malformed. Please login again.");
                return;
            }
        } catch (Exception e) {
            // Log karein par filter chain ko chalne dein taaki permitAll kaam kare
            logger.error("Cannot set user authentication");
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        // Manual JSON response
        response.getWriter().write("{\"message\": \"" + message + "\", \"status\": 401}");
    }
}