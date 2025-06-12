package back.vybz.auth_user.user.application;

import back.vybz.auth_user.common.application.TokenService;
import back.vybz.auth_user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.common.exception.BaseException;
import back.vybz.auth_user.common.jwt.JwtProvider;
import back.vybz.auth_user.common.util.RedisUtil;
import back.vybz.auth_user.kafka.event.UserAuthEvent;
import back.vybz.auth_user.kafka.producer.UserKafkaProducer;
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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final OAuthRepository oAuthRepository;

    private final TokenService tokenService;

    private final JwtProvider jwtProvider;

    private final RedisUtil<String> redisUtil;

    private final UserKafkaProducer userKafkaProducer;

    @Override
    public UserDetails loadUserByUuid(String userUuid) {

        System.out.println("🔥 userUuid 조회 시도: " + userUuid);

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

        Optional<User> userOptional = oAuthRepository.findByProviderIdAndSocialType(providerId, socialType);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getEmail().equals(email)) {
                throw new BaseException(BaseResponseStatus.INVALID_EMAIL_FOR_PROVIDER);
            }

            return tokenService.issueToken(user);
        }

        // 이메일로 이미 가입된 사용자가 있다면 → providerId가 다르면 잘못된 로그인 시도
        if (oAuthRepository.findByEmail(email).isPresent()) {
            throw new BaseException(BaseResponseStatus.INVALID_PROVIDER);
        }

        User newUser = User.builder()
                .userUuid(UUID.randomUUID().toString())
                .providerId(providerId)
                .socialType(socialType)
                .email(email)
                .status(Status.ACTIVE)
                .build();

        User savedUser = oAuthRepository.save(newUser);

        // kafka 보냈지만 제대로 들어가는지 확인 x -> 리턴 에러 핸들러 붙어야 함 ! (트랜잭션 아니기 때문)
        userKafkaProducer.sendUserAuthEvent(UserAuthEvent.builder()
                .userUuid(savedUser.getUserUuid())
                .nickname(requestOAuthSignInDto.getNickname())
                .profileImageUrl(requestOAuthSignInDto.getProfileImageUrl())
                .build());

        return tokenService.issueToken(savedUser);
    }

    @Transactional
    @Override
    public void signOut(String refreshToken) {

        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        refreshToken = refreshToken.replace("Bearer ", "");

        String uuid = jwtProvider.extractSubject(refreshToken);

        redisUtil.delete("Refresh_user:" + uuid);
    }
}
