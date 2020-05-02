package spaceflight.service;

import spaceflight.model.User;

import java.util.List;

public interface UserService {


    User saveUser(User user);

    List<User> findAll();

    User getUserById(Integer id);

    void deleteUserById(Integer id);
}
