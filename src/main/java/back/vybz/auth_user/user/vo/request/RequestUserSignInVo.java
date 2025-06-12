package back.vybz.auth_user.user.vo.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestUserSignInVo {

    private String provider;

    private String providerId;

    private String email;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public RequestUserSignInVo(String provider, String providerId,
                               String email, String nickname, String profileImageUrl) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
