package com.backend.agriculturalsupportapp.service;

import com.backend.agriculturalsupportapp.model.Transaction;
import com.backend.agriculturalsupportapp.model.TransactionType;
import com.backend.agriculturalsupportapp.model.User;
import com.backend.agriculturalsupportapp.repository.TransactionRepository;
import com.backend.agriculturalsupportapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    public final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param transaction - Transaction details
     * @param userId - Owner of the account
     * @return
     * Everytime a transaction is made, it's added to the transactions table
     * All with the details of the user
     */
    public Transaction addTransaction(Transaction transaction, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(("User not found.")));

        Double currentBalance = user.getBalance();
        Double transactionAmount = transaction.getAmount();

        if("REQUEST_PAYMENT".equals(transaction.getTransactionType())) {
            Double newBalance = currentBalance + transactionAmount;
            user.setBalance(newBalance);
        } else if ("WITHDRAW".equals(transaction.getTransactionType())) {
            if (currentBalance >= transactionAmount) {
                Double newBalance = currentBalance - transactionAmount;
                user.setBalance(newBalance);
            } else {
                throw new IllegalArgumentException("Insufficient balance for withdrawal.");
            }
        } else if ("MAKE_PAYMENT".equals(transaction.getTransactionType())) {
            if (currentBalance >= transactionAmount) {
                Double newBalance = currentBalance - transactionAmount;
                user.setBalance(newBalance);
            }
        } else {
            throw new IllegalArgumentException("Invalid transaction type.");
        }

        transaction.setUser(user);
        userRepository.save(user);
        return transactionRepository.save(transaction);
    }

    /**
     *
     * @param user
     * @return Total amount received from payments
     */
    public Double getTotalEarned(User user) {
        List<Transaction> requestPaymentTransactions = transactionRepository.findByUserAndTransactionType(user, "REQUEST_PAYMENT");
        return requestPaymentTransactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     *
     * @param user
     * @return Total amount in Withdraws
     */
    public Double getTotalCredit(User user) {
        List<Transaction> withdrawTransactions = transactionRepository.findByUserAndTransactionType(user, "WITHDRAW");
        return withdrawTransactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     *
     * @param userId
     * @return List of user's transactions
     * Formart - Transaction Object
     */
    public List<Transaction> getUserTransactionsByType(Long userId, TransactionType transactionType) {
        List<Transaction> allTransactions = transactionRepository.findByUserId(userId);

        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getTransactionType().equals(transactionType.name())) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    /**
     *
     * @param user
     * @param transactions
     * @return
     */
    public Double calculateCreditScore(User user, List<Transaction> transactions) {
        double creditScore = 0;
        double balance = user.getBalance();

        if (balance >= 100000.0) {
            creditScore += 1.9;
        } else if (balance >= 50000.0) {
            creditScore += 1.0;
        } else if (balance >= 10000.0) {
            creditScore += 0.4;
        }

        for (Transaction transaction : transactions) {
            TransactionType transactionType = TransactionType.valueOf(transaction.getTransactionType());

            if (transactionType == TransactionType.REQUEST_PAYMENT) {
                creditScore += 0.05 * 10;
            } else if (transactionType == TransactionType.WITHDRAW) {
                creditScore += 0.03 * 10;
            } else if (transactionType == TransactionType.MAKE_PAYMENT) {
                creditScore += 0.02 * 10;
            }
        }

        creditScore = Math.min(10, Math.max(0, creditScore));

        return Math.round(creditScore * 10.0) / 10.0;
    }

}
