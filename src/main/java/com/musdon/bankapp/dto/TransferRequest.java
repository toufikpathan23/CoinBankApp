package com.musdon.bankapp.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
}
