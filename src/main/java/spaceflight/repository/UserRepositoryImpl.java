package spaceflight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spaceflight.model.User;

@Repository
public interface UserRepositoryImpl extends JpaRepository<User, Integer> {

   User findUserByUsername(String username);

   User findUserById(Integer id);

   void deleteUserByUsername(String username);

}
