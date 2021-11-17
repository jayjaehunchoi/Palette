package com.palette.controller.auth;

import com.palette.dto.Token;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenProvider {

    @Value("${spring.security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenInMs;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenInMs;

    public Token createAccessToken(String payload){
        String value = createToken(payload, accessTokenInMs);
        return new Token(value, accessTokenInMs);
    }

    public Token createRefreshToken(){
        byte[] bytes = new byte[7];
        new Random().nextBytes(bytes);
        String payload = new String(bytes, StandardCharsets.UTF_8);
        String value = createToken(payload, refreshTokenInMs);
        return new Token(value, refreshTokenInMs);
    }

    private String createToken(String payload, long expireTime){
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validTime = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token){
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }catch (JwtException e) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.") {
            };
        }
    }

    public boolean isValidToken(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
}
