package dev.gaurav.dynamodbdemo.service;

import dev.gaurav.dynamodbdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.gaurav.dynamodbdemo.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhoneNumber(this.getClass().getName());
        userRepository.save(user);
        return user;
    }

    public User getUser(String id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(String id, User userDetails) {
        User existingUser = getUser(id);

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        // Note: Changing email might affect GSI consistency if not handled carefully,
        // but simple update is fine for this demo.

        if (userDetails.getPassword() != null) {
            existingUser.setPassword(userDetails.getPassword());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.updateUser(existingUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
