package back.vybz.auth_user.user.dto.request;

import back.vybz.auth_user.user.vo.request.RequestUserSignInVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestOAuthSignInDto {

    private String provider;

    private String providerId;

    private String email;

    private String nickname;

    @Builder
    public RequestOAuthSignInDto(String provider, String providerId,
                                 String email, String nickname) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
    }

    public static RequestOAuthSignInDto from(RequestUserSignInVo requestOAuthLoginVo) {
        return RequestOAuthSignInDto.builder()
                .provider(requestOAuthLoginVo.getProvider())
                .providerId(requestOAuthLoginVo.getProviderId())
                .email(requestOAuthLoginVo.getEmail())
                .nickname(requestOAuthLoginVo.getNickname())
                .build();
    }
}
