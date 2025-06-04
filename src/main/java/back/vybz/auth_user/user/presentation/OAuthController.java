package back.vybz.auth_user.user.presentation;

import back.vybz.auth_user.user.application.OAuthService;
import back.vybz.auth_user.common.application.ReissueService;
import back.vybz.auth_user.common.entity.BaseResponseEntity;
import back.vybz.auth_user.common.entity.BaseResponseStatus;
import back.vybz.auth_user.user.dto.request.RequestOAuthSignInDto;
import back.vybz.auth_user.user.dto.response.ResponseUserSignInDto;
import back.vybz.auth_user.user.vo.request.RequestUserSignInVo;
import back.vybz.auth_user.user.vo.response.ResponseUserSignInVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    private final ReissueService reissueService;

    @Operation(
            summary = "User 소셜 로그인 API",
            description = "User 소셜 로그인 API 입니다.",
            tags = {"OAUTH-SERVICE"}
    )
    @PostMapping("/sign-in")
    public BaseResponseEntity<ResponseUserSignInVo> signIn(
            @RequestBody RequestUserSignInVo requestOAuthSignInVo
            ) {

        RequestOAuthSignInDto requestOAuthSignInDto = RequestOAuthSignInDto.from(requestOAuthSignInVo);

        ResponseUserSignInDto responseUserSignInDto = oAuthService.signIn(requestOAuthSignInDto);

        return new BaseResponseEntity<>(ResponseUserSignInVo.from(responseUserSignInDto));
    }

    @Operation(
            summary = "User 액세스 토큰 재발급 API",
            description = "User 리프레시 토큰을 이용해 액세스 토큰을 재발급하는 API 입니다.",
            tags = {"OAUTH-SERVICE"}
    )
    @PostMapping("/reissue")
    public BaseResponseEntity<ResponseUserSignInVo> reissue(
            @RequestHeader("Authorization") String authorization
    ) {

        ResponseUserSignInDto resultDto = reissueService.reissue(authorization);

        return new BaseResponseEntity<>(ResponseUserSignInVo.from(resultDto));
    }

    @Operation(
            summary = "User 로그아웃 API",
            description = "User 로그아웃을 위한 API 입니다.",
            tags = {"OAUTH-SERVICE"}
    )
    @PostMapping("/sign-out")
    public BaseResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authorization
    ) {
        oAuthService.signOut(authorization);

        return new BaseResponseEntity<>(BaseResponseStatus.SUCCESS);
    }
}