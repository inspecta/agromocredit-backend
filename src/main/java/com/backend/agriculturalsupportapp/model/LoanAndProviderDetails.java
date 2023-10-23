package com.backend.agriculturalsupportapp.model;

import java.time.LocalDateTime;

public interface LoanAndProviderDetails {
    String getName();
    Double getAmount();
    LocalDateTime getCreated_at();
}
