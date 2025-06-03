package back.vybz.auth_user.common.application;

import back.vybz.auth_user.common.jwt.JwtProvider;
import back.vybz.auth_user.common.util.RedisUtil;
import back.vybz.auth_user.domain.User;
import back.vybz.auth_user.dto.response.ResponseUserSignInDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;

    private final RedisUtil<String> redisUtil;

    public ResponseUserSignInDto issueToken(User user) {

        String accessToken = jwtProvider.createUserAccessToken(user.getUserUuid());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserUuid());

        redisUtil.save("Refresh_user:" + user.getUserUuid(), refreshToken, 15, TimeUnit.DAYS);

        return ResponseUserSignInDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userUuid(user.getUserUuid())
                .build();
    }
}
