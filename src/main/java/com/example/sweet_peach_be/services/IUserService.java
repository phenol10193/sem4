package com.example.sweet_peach_be.services;

import com.example.sweet_peach_be.models.User;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long userId);
    User createUser(User user);
    User updateUser(Long userId, User userDetails);
    void deleteUser(Long userId);
    String login(String email, String password);

    void registerUser(User user) throws MessagingException, jakarta.mail.MessagingException;

    void verifyEmail(String token);
}