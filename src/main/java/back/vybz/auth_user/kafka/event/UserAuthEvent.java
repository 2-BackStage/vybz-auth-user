package back.vybz.auth_user.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAuthEvent {

    private String userUuid;

    private String nickname;

    @Builder
    public UserAuthEvent(String userUuid, String nickname) {
        this.userUuid = userUuid;
        this.nickname = nickname;
    }
}
