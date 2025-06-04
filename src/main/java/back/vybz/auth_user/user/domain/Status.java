package back.vybz.auth_user.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    WITHDRAWAL("탈퇴"),
    ACTIVE("정상"),
    BANNED("차단");

    private final String status;
}
