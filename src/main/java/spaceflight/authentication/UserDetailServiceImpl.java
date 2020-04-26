package spaceflight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spaceflight.repository.UserRepositoryImpl;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepositoryImpl userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailServiceImpl(UserRepositoryImpl userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

}
