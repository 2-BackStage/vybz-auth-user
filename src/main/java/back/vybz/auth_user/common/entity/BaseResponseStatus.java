package back.vybz.auth_user.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 200: 요청 성공
     **/
    SUCCESS(HttpStatus.OK, true, 200, "요청에 성공하였습니다."),
    EMAIL_CODE_SUCCESS(HttpStatus.OK, true, 201, "이메일 인증코드 발송에 성공하였습니다."),
    EMAIL_CODE_VERIFICATION_SUCCESS(HttpStatus.OK, true, 202, "이메일 인증에 성공하였습니다."),
    SIGN_UP_SUCCESS(HttpStatus.OK, true, 203, "회원가입에 성공하였습니다."),
    SIGN_IN_SUCCESS(HttpStatus.OK, true, 204, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, true, 205, "로그아웃 되었습니다."),
    SUCCESS_MATCH_PASSWORD(HttpStatus.OK, true, 206, "비밀번호가 확인되었습니다."),
    SUCCESS_UPDATE_PASSWORD(HttpStatus.OK, true, 207, "비밀번호가 변경되었습니다."),
    SUCCESS_UPDATE_NICKNAME(HttpStatus.OK, true, 208, "닉네임이 변경되었습니다."),
    SUCCESS_RECENT_VIEW(HttpStatus.OK, true, 209, "최근 본 상품이 등록되었습니다."),
    SUCCESS_WITHDRAWAL_USER(HttpStatus.OK, true, 210, "회원 탈퇴가 완료되었습니다. 2주안에 재 로그인 시 계정 복구가 가능합니다."),
    SUCCESS_ACCOUNT_RECOVERY(HttpStatus.OK, true, 211, "계정 복구가 완료되었습니다. 로그인 해주세요."),
    SMS_CODE_SUCCESS(HttpStatus.OK, true, 212, "sms 인증코드 발송에 성공하였습니다."),
    SMS_CODE_VERIFICATION_SUCCESS(HttpStatus.OK, true, 213, "sms 인증에 성공하였습니다."),
    NO_OAUTH_USER(HttpStatus.OK, true, 2200, "소셜 계정이 존재하지 않습니다. 추가 정보를 입력해 회원가입 해주세요."),
    SUCCESS_DOWNLOAD_COUPON(HttpStatus.OK, true, 2201, "쿠폰 다운로드에 성공하였습니다."),
    SUCCESS_USE_COUPON(HttpStatus.OK, true, 2202, "쿠폰 사용에 성공하였습니다."),

    /**
     * 400 : security 에러
     */
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, false, 401, "토큰이 유효하지 않습니다"),
    NO_SIGN_IN(HttpStatus.UNAUTHORIZED, false, 402, "로그인을 먼저 진행해주세요"),
    NO_ACCESS_AUTHORITY(HttpStatus.FORBIDDEN, false, 403, "접근 권한이 없습니다"),
    DISABLED_USER(HttpStatus.FORBIDDEN, false, 404, "비활성화된 계정입니다. 계정을 복구하시겠습니까?"),
    FAILED_TO_RESTORE(HttpStatus.INTERNAL_SERVER_ERROR, false, 405, "계정 복구에 실패했습니다. 관리자에게 문의해주세요."),
    NO_EXIST_OAUTH(HttpStatus.NOT_FOUND, false, 406, "사용자 로그인 정보가 존재하지 않습니다."),
    NO_EXIST_AUTH(HttpStatus.NOT_FOUND, false, 412, "버스커 로그인 정보가 존재하지 않습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, false, 407, "이메일 또는 패스워드를 다시 확인해주세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, false, 408, "Refresh Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, false, 409, "Refresh Token이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, false, 410, "유효하지 않은 Access Token입니다."),
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, false, 411, "아이디 또는 비밀번호가 일치하지 않습니다."),
    EXPIRED_OR_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, false, 412, "만료되었거나 위조된 토큰입니다."),
    TOKEN_MISMATCH_WITH_REDIS(HttpStatus.UNAUTHORIZED, false, 413, "Redis에 저장된 액세스 토큰과 일치하지 않습니다."),
    TOKEN_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, false, 414, "토큰에 담긴 유저 정보를 찾을 수 없습니다."),


    /**
     * 900: 기타 에러
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, 900, "Internal server error"),
    SSE_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 901, "알림 전송에 실패하였습니다."),
    LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, false, 902, "로그인에 실패하였습니다."),

    /**
     * 2000: user 에러
     */
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, false, 2001, "이미 사용 중인 이메일입니다."),
    SIGN_UP_NOT_VERIFIED(HttpStatus.BAD_REQUEST, false, 2002, "회원가입 인증이 만료되었습니다. 이메일을 인증을 다시 요청해주세요."),
    SIGN_UP_NOT_SMS_VERIFIED(HttpStatus.BAD_REQUEST, false, 2002, "회원가입 인증이 만료되었습니다. 전화번호 인증을 다시 요청해주세요."),
    DUPLICATED_SMS(HttpStatus.CONFLICT, false, 2003, "이미 사용 중인 전화번호입니다."),
    WITHDRAWAL_PENDING(HttpStatus.BAD_REQUEST, false, 2020, "탈퇴 예정 계정입니다. 로그인을 원하시면 계정 복구를 진행해주세요."),

    /**
     * 2100: mail 에러
     */
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, false, 2117, "인증코드가 틀렸습니다."),
    EXPIRED_EMAIL_CODE(HttpStatus.BAD_REQUEST, false, 2118, "틀린 인증코드 또는 잘못된 인증코드입니다."),
    EMAIL_CODE_SEND_LIMITED(HttpStatus.TOO_MANY_REQUESTS, false, 2119,"이메일 발송은 3분에 1회 입니다. 잠시 후 다시 시도해주세요."),
    EMAIL_CODE_VERIFICATION_LIMITED(HttpStatus.TOO_MANY_REQUESTS, false, 2120, "반복적인 인증에 실패했습니다. 다시 코드 전송을 요청해주세요."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, false, 2121, "가입되지 않은 이메일입니다."),


    /**
     * 2200 : sms 에러
     */
    INVALID_SMS_CODE(HttpStatus.BAD_REQUEST, false, 2117, "인증코드가 틀렸습니다."),
    EXPIRED_SMS_CODE(HttpStatus.BAD_REQUEST, false, 2118, "틀린 인증코드 또는 잘못된 인증코드입니다."),
    SMS_CODE_VERIFICATION_LIMITED(HttpStatus.TOO_MANY_REQUESTS, false, 2119, "반복적인 인증에 실패했습니다. 다시 코드 전송을 요청해주세요."),


    /**
     * Request 유효성 에러
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, false, 1000, "잘못된 요청입니다.");

    private final HttpStatusCode httpStatusCode;
    private final boolean isSuccess;
    private final int code;
    private final String message;
}
