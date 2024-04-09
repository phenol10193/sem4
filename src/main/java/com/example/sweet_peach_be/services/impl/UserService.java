package com.example.sweet_peach_be.services.impl;

import com.example.sweet_peach_be.exceptions.ResourceNotFoundException;
import com.example.sweet_peach_be.models.User;
import com.example.sweet_peach_be.repositories.UserRepository;
import com.example.sweet_peach_be.services.EmailService;
import com.example.sweet_peach_be.services.IUserService;
import com.example.sweet_peach_be.services.TokenService;
import com.example.sweet_peach_be.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JwtService jwtService;

    public List<User> getAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findByIdAndIsDeletedFalse(userId);
    }

    public User createUser(User user) {
        // Kiểm tra xem người dùng đã tồn tại hay chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Lưu người dùng vào cơ sở dữ liệu
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        // Update user details
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());

        // Save and return updated user
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmailAndPasswordAndIsDeletedFalse(email, password);
        if (user == null) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        return jwtService.generateToken(email);
    }


    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    public void registerUser(User user) throws MessagingException, jakarta.mail.MessagingException {
        // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo mã xác thực tạm thời
        String token = UUID.randomUUID().toString();

        // Lưu mã xác thực vào Redis với thời gian hết hạn là 3 phút
        tokenService.storeToken(token, 3);

        // Gửi email xác thực
        sendVerificationEmail(user.getEmail(), token);
    }

    private void sendVerificationEmail(String email, String token) throws MessagingException, jakarta.mail.MessagingException {
        // Triển khai logic để gửi email xác thực
        String verificationLink = "http://" + serverAddress + ":" + serverPort + "/verify?token=" + token;
        String emailBody = "Please click on the link to verify your email: " + verificationLink;
        emailService.sendEmail(email, "Email Verification", emailBody);
    }

    public void verifyEmail(String token) {
        // Kiểm tra xem mã xác thực có hợp lệ không
        if (!tokenService.isTokenValid(token)) {
            throw new RuntimeException("Invalid verification token");
        }

        // Xác thực thành công, lưu người dùng vào cơ sở dữ liệu
        String email = tokenService.getEmailFromToken(token);
        User user = new User();
        user.setEmail(email);
        userRepository.save(user);

        // Xoá mã xác thực khỏi Redis sau khi đã sử dụng
        tokenService.removeToken(token);
    }
}