package back.vybz.auth_user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("구글"),
    KAKAO("카카오");

    private final String type;
}