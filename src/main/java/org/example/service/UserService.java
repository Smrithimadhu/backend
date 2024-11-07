package org.example.service;

import org.example.exception.AccessDeniedException;
import org.example.exception.UserAlreadyExistsException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists.");
        }
        // Password encoding removed (insecure!)
        return userRepository.save(user);
    }


    public User updateUser(Integer userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            return userRepository.save(user);
        } else {
            throw new AccessDeniedException("You are not authorized to update this user details."); //This exception needs to be defined
        }
    }


    public void deleteUser(Integer userId) {
        // Delete associated blogs first
        jdbcTemplate.update("DELETE FROM blogs WHERE user_id = ?", userId);

        // Delete the user
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);

    }


    public User getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}