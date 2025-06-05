package back.vybz.auth_user.user.domain;

import back.vybz.auth_user.common.entity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 uuid
     */
    @Column(name = "user_uuid", nullable = false, unique = true)
    private String userUuid;

    /**
     * 소셜 타입
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    /**
     * 소셜 id
     */
    @Column(name = "provider_id", unique = true, nullable = false, length = 30)
    private String providerId;

    /**
     * 이메일
     */
    @Column(name = "email", nullable = false, unique = true, length = 30)
    private String email;

    /**
     * 사용자 상태
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public User(Long id, String userUuid, SocialType socialType, String providerId,
                String email, Status status) {
        this.id = id;
        this.userUuid = userUuid;
        this.socialType = socialType;
        this.providerId = providerId;
        this.email = email;
        this.status = status;
    }
}
