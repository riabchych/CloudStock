package com.riabchych.cloudstock.util;

import com.riabchych.cloudstock.entity.Token;
import com.riabchych.cloudstock.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenUtilImpl implements TokenUtil {

    private Clock clock = DefaultClock.INSTANCE;

    private String secret;
    private long expiration;

    @Value("${cloudstock.token.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${cloudstock.token.expiration}")
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    @Override
    public Token getJWTToken(String username, String[] roles) {
        if (username == null) {
            throw new NullPointerException("null username is illegal");
        }
        if (roles == null) {
            throw new NullPointerException("null roles are illegal");
        }
        if (expiration == 0) {
            throw new NullPointerException("null expires is illegal");
        }
        if (secret == null) {
            throw new NullPointerException("null key is illegal");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        Token token = new Token();
        token.setExpires(expirationDate);
        token.setAuthToken(Jwts
                .builder()
                .setIssuer("Jersey-Security-Basic")
                .setSubject(username)
                .setAudience(StringUtils.join(Arrays.asList(roles), ','))
                .setExpiration(token.getExpires())
                .setIssuedAt(createdDate)
                .signWith(signatureAlgorithm, secret)
                .compact());
        return token;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }

    @Override
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    @Override
    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public Boolean validateToken(String token, User user) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

}