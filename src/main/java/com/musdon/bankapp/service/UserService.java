package com.musdon.bankapp.service;

import com.musdon.bankapp.dto.TransactionRequest;
import com.musdon.bankapp.dto.Response;
import com.musdon.bankapp.dto.UserRequest;

import java.util.List;

public interface UserService {
    Response registerUser(UserRequest userRequest);
    List<Response> allUsers();
    Response fetchUser(Long id);
    Response balanceEnquiry(String accountNumber);
    Response nameEnquiry(String accountNumber);
    Response credit(TransactionRequest transactionRequest);
    Response debit(TransactionRequest transactionRequest);
}
