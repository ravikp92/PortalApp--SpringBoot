package com.ravi.assignment.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {
  private static final long serialVersionUID = -2550185165626007488L;
  
  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  
  @Value("${jwt.secret}")
  private String secret;
  
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }
  
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }
  
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }
  
  private Claims getAllClaimsFromToken(String token) {
    return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
  }
  
  private Boolean isTokenExpired(String token) {
    Date expiration = getExpirationDateFromToken(token);
    return Boolean.valueOf(expiration.before(new Date()));
  }
  
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }
  
  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
      .signWith(SignatureAlgorithm.HS512, this.secret).compact();
  }
  
  public Boolean validateToken(String token, UserDetails userDetails) {
	  final String username = getUsernameFromToken(token);
	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));}
}
