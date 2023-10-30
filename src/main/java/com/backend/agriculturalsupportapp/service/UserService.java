package com.backend.agriculturalsupportapp.service;

import com.backend.agriculturalsupportapp.model.User;
import com.backend.agriculturalsupportapp.repository.TransactionRepository;
import com.backend.agriculturalsupportapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntity<?> registerUser(User user) {
        /**
         *  Check if the phone number is already registered
         */
        Optional<User> userOptional = userRepository.findUserByPhoneNumber(user.getPhoneNumber());

        if (userOptional.isPresent()) {
            String errorMessage = "Sorry. You are already registered.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        // Set encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        if (savedUser != null) {
            savedUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } else {
            String errorMessage = "Could not register user.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber);
    }

    public Double getUserTotalAmount(Long userId) {
        Double totalAmount = transactionRepository.getTotalAmountByUserId(userId);
        return totalAmount != null ? totalAmount : 0.0;
    }

    public String deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
            return "User deleted";
        }

        return "User not found";
    }

}
