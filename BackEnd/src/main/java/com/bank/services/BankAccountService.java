package com.bank.services;

import com.bank.dtos.*;
import com.bank.entities.*;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFound;
import com.bank.exceptions.CustomerNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers(int page);

    List<Customer> listCustomer();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFound;

    void debit(String accountId, double amount, String description,String upiId) throws BalanceNotSufficientException, BankAccountNotFound;

    void credit(String accountId, double amount, String description) throws BankAccountNotFound;

    void transfer(String accountIdSource, String accountIdDestination, double amount,String description) throws BankAccountNotFound, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountListOfCustomer(Long customerId);

    BankAccountsDTO getBankAccountList(int page);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountOperationHistory(String accountId);

    AccountHistoryDTO getAccoutHistory(String accountId, int page, int size) throws BankAccountNotFound;

    CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException;

    BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO);

    CustomerDTO getCustomerByName(String name);
}
