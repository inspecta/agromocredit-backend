package com.backend.agriculturalsupportapp.service;

import com.backend.agriculturalsupportapp.model.*;
import com.backend.agriculturalsupportapp.repository.LoanProviderRepository;
import com.backend.agriculturalsupportapp.repository.LoanRepository;
import com.backend.agriculturalsupportapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanProviderRepository loanProviderRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, LoanProviderRepository loanProviderRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.loanProviderRepository = loanProviderRepository;
    }

    public ResponseEntity<String> saveLoan(Loan loan, Long user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Set the user who took one the loan
            Double loanAmount = loan.getAmount();
            Double userBalance = user.getBalance();

            loan.setUser(user);

            user.setBalance(userBalance + loanAmount);

            // Add 8% loan percentage
            loan.setAmount(loanAmount * 1.08);

            userRepository.save(user);
            loanRepository.save(loan);

            return ResponseEntity.ok("Loan saved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    public ResponseEntity<String> saveLoanProvider(LoanProvider loanProvider) {
        loanProviderRepository.save(loanProvider);
        return ResponseEntity.ok("Loan provider saved!");
    }

    public List<LoanAndProviderDetails> getLoanDetailsByUserId(Long userId) {
        return loanRepository.findAmountLoanProviderNameAndCreatedAtByUserId(userId);
    }

    public List<Loan> getUserLoans(Long userId) {
        System.out.println(loanRepository.findByUserId(userId));
        return loanRepository.findByUserId(userId);
    }

    public Double calculateTotalLoansForUser(Long userId) {
        List<Loan> userLoans = loanRepository.findByUserId(userId);
        Double totalLoans = 0.0;

        for (Loan loan : userLoans) {
            totalLoans += loan.getAmount();
        }

        return totalLoans;
    }

}
