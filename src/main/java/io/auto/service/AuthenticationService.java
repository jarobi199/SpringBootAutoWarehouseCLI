package io.auto.service;

import io.auto.authentication.PasswordEncryptor;
import io.auto.authentication.SessionContext;
import io.auto.enums.Role;
import io.auto.model.User;
import io.auto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String passwordCandidate) {
        boolean authenticated = false;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if ((optionalUser.isPresent()) && PasswordEncryptor.authenticate(passwordCandidate, optionalUser.get().getPassword())) {
            User user = optionalUser.get();
            authenticated = true;
            SessionContext.login(user);
        }

        return authenticated;
    }

    public void initializeUser(String name, String username, String password, Role role, int threshold) {
        User user = new User(name, username, PasswordEncryptor.encrypt(password), role, threshold);
        userRepository.save(user);
    }

}
