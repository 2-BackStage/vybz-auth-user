package back.vybz.auth_user.user.application;

import back.vybz.auth_user.user.dto.request.RequestOAuthSignInDto;
import back.vybz.auth_user.user.dto.response.ResponseUserSignInDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface OAuthService {

    UserDetails loadUserByUuid(String userUuid);

    ResponseUserSignInDto signIn(RequestOAuthSignInDto requestOAuthSignUpDto);

    void signOut(String refreshToken);
}
