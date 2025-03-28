package com.springboot.app.security;

import com.springboot.app.security.user_principle.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    public static final Logger log= LoggerFactory.getLogger(JwtProvider.class);
    private final String JWT_SECRET="secretaaaaaaaaaaaaahhhhhhhhh" +
            "hhhhhhhhhhhhhhhhhhhhhaaaaaaaaaaaaaaatttttttttttttttttttttttttttttttttt";
    private final long JWT_EXPIRATION=86400000L;

    //create token from user
    public String generateToken(Authentication authentication){
        Date now = new Date();
        Date expiryDate=new Date(now.getTime()+JWT_EXPIRATION);
        UserPrincipal userPrincipal =(UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        //create jwt
        return Jwts.builder()
                .claim("id", userPrincipal.getId().toString())
                .setSubject(userPrincipal.getUsername())
                .claim("roles",roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith( key(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }
    //get user from jwt
    public String getUserFromJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }
        catch (Exception e) {
            throw new JwtException("Invalid JWT token");
        }
    }
    public boolean validateToken(String authToken) {
        Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
        return true;
    }

    public UserPrincipal getUserPrincipal(String token) {
        try {
            var data = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
            return new UserPrincipal(
                    Long.parseLong(data.get("id", String.class)),
                    "",
                    data.getSubject(),
                    0,
                    getRoleFromToken(data).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet())
            );
        }
        catch (Exception e) {
            throw new JwtException("Invalid JWT token");
        }
    }

    public List<String> getRoleFromToken(Claims claims){
        return claims.get("roles",List.class);
    }
}
