package spaceflight.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spaceflight.authentication.model.User;

import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Integer> {

   User findUserByUsername(@Param("inputUsername") String inputUsername);
}
