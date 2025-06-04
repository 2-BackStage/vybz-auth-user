package back.vybz.auth_user.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseUserSignInDto {

    private String accessToken;

    private String refreshToken;

    private String userUuid;

    @Builder
    public ResponseUserSignInDto(String accessToken, String refreshToken, String userUuid) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userUuid = userUuid;
    }
}
