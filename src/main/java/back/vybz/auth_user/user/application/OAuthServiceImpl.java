package back.vybz.auth_user.user.application;

import back.vybz.auth_user.common.application.TokenService;
import back.vybz.auth_user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.common.exception.BaseException;
import back.vybz.auth_user.common.jwt.JwtProvider;
import back.vybz.auth_user.common.util.RedisUtil;
import back.vybz.auth_user.user.domain.CustomUserDetails;
import back.vybz.auth_user.user.domain.SocialType;
import back.vybz.auth_user.user.domain.Status;
import back.vybz.auth_user.user.domain.User;
import back.vybz.auth_user.user.dto.request.RequestOAuthSignInDto;
import back.vybz.auth_user.user.dto.response.ResponseUserSignInDto;
import back.vybz.auth_user.user.infrastructure.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final OAuthRepository oAuthRepository;

    private final TokenService tokenService;

    private final JwtProvider jwtProvider;

    private final RedisUtil<String> redisUtil;

    @Override
    public UserDetails loadUserByUuid(String userUuid) {
        return oAuthRepository.findByUserUuid(userUuid)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("해당 UUID 사용자를 찾을 수 없습니다."));
    }

    @Transactional
    @Override
    public ResponseUserSignInDto signIn(RequestOAuthSignInDto requestOAuthSignInDto) {

        String providerId = requestOAuthSignInDto.getProviderId();
        String email = requestOAuthSignInDto.getEmail();

        SocialType socialType = SocialType.valueOf(requestOAuthSignInDto.getProvider().toUpperCase());

        User user = oAuthRepository.findBySocialTypeAndProviderId(socialType, providerId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userUuid(UUID.randomUUID().toString())
                            .providerId(providerId)
                            .socialType(socialType)
                            .email(email)
                            .status(Status.ACTIVE)
                            .build();

                    return oAuthRepository.save(newUser);
                });

        return tokenService.issueToken(user);
    }

    @Transactional
    @Override
    public void signOut(String refreshToken) {

        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        refreshToken = refreshToken.replace("Bearer ", "");

        String uuid = jwtProvider.extractSubject(refreshToken);

        redisUtil.delete("Access:" + uuid);
        redisUtil.delete("Refresh:" + uuid);
    }
}
