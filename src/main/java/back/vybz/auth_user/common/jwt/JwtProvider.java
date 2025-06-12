package back.vybz.auth_user.common.jwt;

import back.vybz.auth_user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.common.exception.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final Environment env;

    public String extractSubject(String token) throws IllegalArgumentException {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (NullPointerException e) {
            throw new BaseException(BaseResponseStatus.TOKEN_USER_NOT_FOUND);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractTokenType(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("token_type", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String createUserAccessToken(String userUuid) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + env.getProperty("JWT.token.access-expire-time", Long.class, 1800000L));

        return Jwts.builder()
                .claim("user_uuid", userUuid)
                .claim("token_type", "access")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(String userUuid) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + env.getProperty("JWT.token.refresh-expire-time", Long.class, 1296000000L));

        return Jwts.builder()
                .subject(userUuid)
                .claim("token_type", "refresh")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public SecretKey getSignKey() {
        String secret = env.getProperty("JWT.secret-key");
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public void validateToken(String token) {
        try {
            extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            log.warn("❌ 만료된 JWT 토큰입니다: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.warn("❌ 지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
        } catch (MalformedJwtException e) {
            log.warn("❌ 잘못된 형식의 JWT 토큰입니다: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.warn("❌ JWT claims 비어있습니다: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
        } catch (Exception e) {
            log.warn("❌ 유효하지 않은 JWT: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
        }
    }
}