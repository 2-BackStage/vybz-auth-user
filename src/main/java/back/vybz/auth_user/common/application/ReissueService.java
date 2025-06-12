package back.vybz.auth_user.common.application;

import back.vybz.auth_user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.common.exception.BaseException;
import back.vybz.auth_user.common.jwt.JwtProvider;
import back.vybz.auth_user.common.util.RedisUtil;
import back.vybz.auth_user.user.domain.User;
import back.vybz.auth_user.user.dto.response.ResponseUserSignInDto;
import back.vybz.auth_user.user.infrastructure.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtProvider jwtProvider;

    private final RedisUtil<String> redisUtil;

    private final OAuthRepository oAuthRepository;

    private final TokenService tokenService;

    public ResponseUserSignInDto reissue(String authorization) {

        String refreshToken = parseAndValidate(authorization);

        String userUuid = jwtProvider.extractSubject(refreshToken);

        validateRedisToken("Refresh_user:" + userUuid, refreshToken);

        User user = oAuthRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_OAUTH));

        return tokenService.issueToken(user);
    }

    // Authorization 헤더에서 Refresh Token 추출 및 유효성 검사
    private String parseAndValidate(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        String refreshToken = authorization.replace("Bearer ", "");

        jwtProvider.validateToken(refreshToken);

        return refreshToken;
    }

    // Redis 저장된 Refresh Token 실제 요청된 토큰과 일치하는지 확인
    private void validateRedisToken(String redisKey, String refreshToken) {
        String redisToken = redisUtil.get(redisKey);
        if (redisToken == null || !redisToken.equals(refreshToken)) {
            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_NOT_FOUND);
        }
    }
}
