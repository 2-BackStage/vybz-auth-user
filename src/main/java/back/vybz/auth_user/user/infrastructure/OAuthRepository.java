package back.vybz.auth_user.user.infrastructure;

import back.vybz.auth_user.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserUuid(String userUuid);

    Optional<User> findByEmail(String email);
}
