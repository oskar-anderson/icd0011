package conf.security.jwt;

import conf.security.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtHelper {

    private String key;

    public JwtHelper(String key) {
        this.key = key;
    }

    public String encode(TokenInfo tokenInfo) {
        return encode(tokenInfo, LocalDateTime.now().plusMinutes(15));
    }

    public String encode(TokenInfo tokenInfo, LocalDateTime expiration) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                .setSubject(tokenInfo.getUserName())
                .setExpiration(asDate(expiration))
                .claim("roles", tokenInfo.getRolesAsString())
                .compact();

    }

    public TokenInfo decode(String token) {

        token = token.replace("Bearer ", "");

        Claims body = Jwts.parserBuilder()
                .setSigningKey(key.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new TokenInfo(
                body.getSubject(),
                body.get("roles", String.class));
    }

    private Date asDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
