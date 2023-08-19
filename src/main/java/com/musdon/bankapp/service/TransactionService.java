package com.musdon.bankapp.service;

import com.musdon.bankapp.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
