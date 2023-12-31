package com.backend.agriculturalsupportapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Amount of loan that was taken out
     * Maximum of 500K
     */
    private Double amount;
    /**
     * Percentage depending on the Loan provider
     */
    @Column(name = "interest_rate")
    @JsonProperty("interest_rate")
    private Double interestRate;

    /**
     * The user who took out the loan
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @PrePersist
    private void onCreate() {
        created_at = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "loan_provider_id")
    @JsonProperty("loan_provider_id")
    private LoanProvider loanProvider;

    @Column(name = "type")
    private String type = "GET_LOAN";
}
