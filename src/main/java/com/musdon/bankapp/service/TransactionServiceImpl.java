package com.musdon.bankapp.service;

import com.musdon.bankapp.dto.Data;
import com.musdon.bankapp.dto.Response;
import com.musdon.bankapp.dto.TransactionDto;
import com.musdon.bankapp.entity.Transaction;
import com.musdon.bankapp.repository.TransactionRepository;
import com.musdon.bankapp.utils.ResponseUtils;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto transaction) {

        Transaction newTransaction = Transaction.builder()
                .transactionType(transaction.getTransactionType())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .build();

        transactionRepository.save(newTransaction);
    }
}
