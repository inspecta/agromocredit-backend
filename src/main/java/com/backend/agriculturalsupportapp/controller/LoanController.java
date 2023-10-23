package com.backend.agriculturalsupportapp.controller;

import com.backend.agriculturalsupportapp.model.Loan;
import com.backend.agriculturalsupportapp.model.LoanAndProviderDetails;
import com.backend.agriculturalsupportapp.model.LoanProvider;
import com.backend.agriculturalsupportapp.model.User;
import com.backend.agriculturalsupportapp.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.1.117:8081")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/save-loan/{user_id}")
    public ResponseEntity<String> addLoan(@RequestBody Loan loan, @PathVariable Long user_id) {
        return loanService.saveLoan(loan, user_id);
    }

    @PostMapping("/save-loan-provider")
    public ResponseEntity<String> saveLoanProvider(@RequestBody LoanProvider loanProvider) {
        return loanService.saveLoanProvider(loanProvider);
    }

    @GetMapping("/loans/{userId}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable Long userId) {
        List<Loan> userLoans = loanService.getUserLoans(userId);
        return ResponseEntity.ok(userLoans);
    }

    @GetMapping("/user-loans/{userId}")
    public ResponseEntity<List<LoanAndProviderDetails>> getLoanDetailsByUserId(@PathVariable Long userId) {
        List<LoanAndProviderDetails> loanDetails = loanService.getLoanDetailsByUserId(userId);
        return ResponseEntity.ok(loanDetails);
    }

    @GetMapping("/total-loans/{userId}")
    public Double getTotalLoansForUser(@PathVariable Long userId) {
        User user = new User();
        return loanService.calculateTotalLoansForUser(userId);
    }
}
