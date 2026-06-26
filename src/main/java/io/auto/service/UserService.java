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
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void changePassword(String newPassword) {
        String encodedPassword = PasswordEncryptor.encrypt(newPassword);
        SessionContext.getUser().setPassword(encodedPassword);
        userRepository.save(SessionContext.getUser());
        System.out.println("Your password has been changed!");
    }

    public void addUser(String fullName, String username, String password, Role role, int highValue) {
        User user = new User(fullName, username, PasswordEncryptor.encrypt(password), role, highValue);
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        Optional<User> toDelete = userRepository.findByUsername(username);
        toDelete.ifPresent(user -> {
            userRepository.delete(user);
            System.out.println("User " + username + " has been deleted!");
        });
    }

    public void setHighValueThreshold(int highValue) {
        SessionContext.getUser().setHighValueThreshold(highValue);
        userRepository.save(SessionContext.getUser());
        System.out.println("High value threshold has been set to $" + highValue);
    }
}
