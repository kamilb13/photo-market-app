package pl.photomarketapp.photomarketapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.model.User;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final String SECRET_KEY = "6LJsHL0QnYxtSuNipfuD6iHLT9gzTmiGUsVbvc1/O5Y=";
    private final byte[] key = Base64.getDecoder().decode(SECRET_KEY);

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(SECRET_KEY))
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(SECRET_KEY))
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}
