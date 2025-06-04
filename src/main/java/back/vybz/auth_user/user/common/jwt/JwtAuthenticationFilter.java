package back.vybz.auth_user.user.common.jwt;

import back.vybz.auth_user.user.application.OAuthService;
import back.vybz.auth_user.user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.user.common.exception.BaseException;
import back.vybz.auth_user.user.common.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final RedisUtil<String> redisUtil;

    private final OAuthService oAuthService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {

            if (!jwtProvider.isValidToken(jwt)) {
                throw new BaseException(BaseResponseStatus.EXPIRED_OR_INVALID_TOKEN);
            }

            String tokenType = jwtProvider.extractTokenType(jwt);
            String userUuid = jwtProvider.extractClaim(jwt, claims -> claims.get("user_uuid", String.class));


            if (userUuid != null && "access".equals(tokenType)) {
                String redisKey = "Access_user:" + userUuid;
                String redisAccessToken = redisUtil.get(redisKey);

                if (redisAccessToken == null || !redisAccessToken.equals(jwt)) {
                    throw new BaseException(BaseResponseStatus.TOKEN_MISMATCH_WITH_REDIS);
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = oAuthService.loadUserByUuid(userUuid);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.equals("/api/v1/oauth/reissue") ||
                uri.equals("/api/v1/oauth/sign-out") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/webjars") ||
                uri.startsWith("/error");
    }
}