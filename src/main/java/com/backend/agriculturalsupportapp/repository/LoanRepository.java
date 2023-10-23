package com.backend.agriculturalsupportapp.repository;

import com.backend.agriculturalsupportapp.model.Loan;
import com.backend.agriculturalsupportapp.model.LoanAndProviderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = "SELECT (p.name, l.amount, l.created_at) FROM\n" +
            "loans l JOIN loan_provider p ON l.loan_provider_id = p.id\n" +
            "WHERE l.user_id = :userId", nativeQuery = true)
    List<LoanAndProviderDetails> findAmountLoanProviderNameAndCreatedAtByUserId(@Param("userId") Long userId);

    List<Loan> findByUserId(Long userId);
}
