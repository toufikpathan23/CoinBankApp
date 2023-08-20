package com.bank.controllers;


import com.bank.dtos.*;
import com.bank.entities.AccountOperation;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFound;
import com.bank.services.BankAccountServiceImplementation;
import com.bank.services.BankStatement;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController

@CrossOrigin("*")
public class BankAccountRestController {

    @Autowired
    BankAccountServiceImplementation bankAccountServiceImplementation;
    
    @Autowired
    private BankStatement bankStatement;

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFound {
        return bankAccountServiceImplementation.getBankAccount(accountId);
    }

    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/Account/searchAccount")
    public BankAccountsDTO getBankAccount(@RequestParam(name = "page", defaultValue = "0") int page) throws BankAccountNotFound {
        return bankAccountServiceImplementation.getBankAccountList(page);
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getBankAccountOperations(@PathVariable String accountId, @RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFound {
        return bankAccountServiceImplementation.getAccoutHistory(accountId, page, size);
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/operations/Debit")
    public AccountOperationDTO debit(@RequestBody AccountOperationDTO accountOperarionDTO) throws BankAccountNotFound, BalanceNotSufficientException {
        this.bankAccountServiceImplementation.debit(accountOperarionDTO.getAccountId(), accountOperarionDTO.getAmount(), accountOperarionDTO.getDescription());
        return accountOperarionDTO;
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/operations/Credit")
    public AccountOperationDTO credit(@RequestBody AccountOperationDTO accountOperarionDTO) throws BankAccountNotFound, BalanceNotSufficientException {
        this.bankAccountServiceImplementation.credit(accountOperarionDTO.getAccountId(), accountOperarionDTO.getAmount(), accountOperarionDTO.getDescription());
        return accountOperarionDTO;
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/operations/Transfers")
    public void transfers(@RequestParam(name = "idSource") String idSource, @RequestParam(name = "idDestination") String idDestination, @RequestParam(name = "amount") double amount,@RequestParam(name = "description") String description) throws BankAccountNotFound, BalanceNotSufficientException {
        this.bankAccountServiceImplementation.transfer(idSource, idDestination, amount,description);

    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFound, BalanceNotSufficientException {
        System.out.println(debitDTO.toString());
        this.bankAccountServiceImplementation.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFound {
        this.bankAccountServiceImplementation.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFound, BalanceNotSufficientException {
        this.bankAccountServiceImplementation.transfer(transferRequestDTO.getAccountSource(), transferRequestDTO.getAccountDestination(), transferRequestDTO.getAmount(),transferRequestDTO.getDescription());
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PutMapping("/accounts/{accountId}")
    public BankAccountDTO updateAccount(@PathVariable String accountId, @RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountDTO.setId(accountId);
        return bankAccountServiceImplementation.updateBankAccount(bankAccountDTO);
    }
    
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/statement")
	public List<AccountOperation> generateBankStatement(
			@RequestParam String accountNumber/* , @RequestParam String startDate,@RequestParam String endDate */) throws FileNotFoundException, ParseException, DocumentException
    {
		return bankStatement.generateStatement(accountNumber/* ,startDate,endDate */);
    }
    
}
