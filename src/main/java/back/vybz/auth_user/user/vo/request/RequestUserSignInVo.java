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

    @Builder
    public RequestUserSignInVo(String provider, String providerId,
                               String email, String nickname) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
    }
}
