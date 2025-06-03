package back.vybz.auth_user.vo.response;

import back.vybz.auth_user.dto.response.ResponseUserSignInDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseUserSignInVo {

    private String accessToken;

    private String refreshToken;

    private String userUuid;

    @Builder
    public ResponseUserSignInVo(String accessToken, String refreshToken, String userUuid) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userUuid = userUuid;
    }

    public static ResponseUserSignInVo from(ResponseUserSignInDto responseUserSignInDto) {
        return ResponseUserSignInVo.builder()
                .accessToken(responseUserSignInDto.getAccessToken())
                .refreshToken(responseUserSignInDto.getRefreshToken())
                .userUuid(responseUserSignInDto.getUserUuid())
                .build();
    }
}
