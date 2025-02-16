package com.sena.hidden_pass.application.config;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtFilter extends OncePerRequestFilter {

    // TODO - CHANGE TO REPOSITORY_ADAPTER
    private IUserRepository userRepository;

    public JwtFilter(IUserRepository iUserRepository){
        this.userRepository = iUserRepository;
    }

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("my-ultra-super-key-my-ultra-super-key".getBytes());

    public String generateToken(UUID id){
        return Jwts.builder()
                .signWith(SECRET_KEY)
                .subject(id.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // Una hora
                .compact();
    }

    public String extractSubject(String token){
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().getSubject();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header missing or does not start with 'Bearer '");
            filterChain.doFilter(request,response);
            return;
        };

        String jwt = authHeader.substring(7);
        String subject = extractSubject(jwt);

        if(subject == null || SecurityContextHolder.getContext().getAuthentication() != null){
            System.out.println("Username is null or SecurityContextHolder isn't null");
            filterChain.doFilter(request, response);
            return;
        }

        if(userRepository.findById(UUID.fromString(subject)).isEmpty()) throw new UsernameNotFoundException("Username Not found");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(subject, null, null);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request,response);
    }
}
