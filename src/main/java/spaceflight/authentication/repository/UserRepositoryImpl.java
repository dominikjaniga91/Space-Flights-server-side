package spaceflight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spaceflight.authentication.model.User;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Integer> {

    User findUserByUsername(String username);
}
