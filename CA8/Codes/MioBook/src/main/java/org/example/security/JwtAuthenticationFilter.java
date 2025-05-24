package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/api/login") || path.startsWith("/api/signup") || path.startsWith("/api/google/callback")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // remove "Bearer "

            try {
                // validate token
                Claims claims = JwtUtil.validateToken(token).getBody();

                String userIdStr = claims.getSubject();
                String username = claims.get("username", String.class);
                String email = claims.get("email", String.class);

                // store in context
                UserDetailsFromToken userDetails = new UserDetailsFromToken(
                        Long.parseLong(userIdStr), username, email);
                UserContextHolder.set(userDetails);

                chain.doFilter(request, response);
                return;

            } catch (JwtException | IllegalArgumentException e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Invalid or expired token");
                return;
            } finally {
                UserContextHolder.clear();
            }
        }

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("Missing or invalid Authorization header");
    }
}