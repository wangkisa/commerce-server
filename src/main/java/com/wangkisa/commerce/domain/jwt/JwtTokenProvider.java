package com.wangkisa.commerce.domain.jwt;

import com.wangkisa.commerce.security.JwtModel;
import com.wangkisa.commerce.security.SecurityUserDetailService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final String JWT_SECRET_KEY = Base64.getEncoder().encodeToString("GOVhSBagcq".getBytes());

    private final long ACCESS_TOKEN_EXPIRATION_MS = 120 * 60 * 1000L;
    private final long REFRESH_TOKEN_EXPIRATION_MS = 2 * 24 * 60 * 60 * 1000L;

    private final SecurityUserDetailService securityUserDetailService;

    public JwtModel createToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        Date now = new Date();
        Date accessDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);
        Date refreshDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return JwtModel.builder()
                .accessToken(this.generateToken(claims, now, accessDate))
                .refreshToken(this.generateToken(new HashMap<>(), now, refreshDate))
                .accessTokenExpirationDate(sdf.format(accessDate))
                .refreshTokenExpirationDate(sdf.format(refreshDate))
                .build();
    }

    private String generateToken(Map<String, Object> claims, Date now, Date expirationDate) {
        return Jwts.builder()
                .setIssuedAt(now)
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            this.extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = securityUserDetailService.loadUserByUsername(this.getEmailFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmailFromToken(String token) {
        return this.getClaims(token, "email");
    }

    public Date getExpFromToken(String token) {
        return this.extractAllClaims(token)
                .getBody()
                .get("exp", Date.class);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return getJwtFromAuthorization(authorization);
    }

    public String getJwtFromAuthorization(String authorization) {
        if(StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }

    public Jws<Claims> extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token);
    }

    public String getClaims(String token, String key) {
        return this.extractAllClaims(token)
                .getBody()
                .get(key, String.class);
    }
}
