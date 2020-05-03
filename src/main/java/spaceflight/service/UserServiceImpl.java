package spaceflight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spaceflight.model.User;
import spaceflight.repository.UserRepositoryImpl;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepositoryImpl userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public User updateUser(User user) {

        User tmpUser = userRepository.findUserById(user.getId());
        tmpUser.setUsername(user.getUsername());
        tmpUser.setPassword(passwordEncoder.encode(user.getPassword()));
        tmpUser.setRole(user.getRole());

        return userRepository.save(tmpUser);
    }
}
