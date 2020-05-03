package spaceflight.service;

import spaceflight.model.User;

import java.util.List;

public interface UserService {


    User saveUser(User user);

    List<User> findAll();

    User getUserByUsername(String username);

    void deleteUserByUsername(String username);

    User updateUser(User user);
}
