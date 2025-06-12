package back.vybz.auth_user.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAuthEvent {

    private String userUuid;

    private String nickname;

    private String profileImageUrl;

    @Builder
    public UserAuthEvent(String userUuid, String nickname, String profileImageUrl) {
        this.userUuid = userUuid;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
