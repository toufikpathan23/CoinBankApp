package com.bank.controllers;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dtos.AccountHistoryDTO;
import com.bank.dtos.AccountOperationDTO;
import com.bank.dtos.BankAccountDTO;
import com.bank.dtos.BankAccountsDTO;
import com.bank.dtos.CreditDTO;
import com.bank.dtos.DebitDTO;
import com.bank.dtos.TransferRequestDTO;
import com.bank.entities.AccountOperation;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFound;
import com.bank.security.services.AccountService;
import com.bank.security.services.AccountServiceImpl;
import com.bank.services.BankAccountServiceImplementation;
import com.bank.services.BankStatement;
import com.itextpdf.text.DocumentException;

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
	public BankAccountsDTO getBankAccount(@RequestParam(name = "page", defaultValue = "0") int page)
			throws BankAccountNotFound {
		return bankAccountServiceImplementation.getBankAccountList(page);
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoryDTO getBankAccountOperations(@PathVariable String accountId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFound {
		return bankAccountServiceImplementation.getAccoutHistory(accountId, page, size);
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/operations/Debit")
	public AccountOperationDTO debit(@RequestBody AccountOperationDTO accountOperarionDTO)
			throws BankAccountNotFound, BalanceNotSufficientException {
		this.bankAccountServiceImplementation.debit(accountOperarionDTO.getAccountId(), accountOperarionDTO.getAmount(),
				accountOperarionDTO.getDescription(),"fromaccountoperationsdto");
		return accountOperarionDTO;
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/operations/Credit")
	public AccountOperationDTO credit(@RequestBody AccountOperationDTO accountOperarionDTO)
			throws BankAccountNotFound, BalanceNotSufficientException {
		this.bankAccountServiceImplementation.credit(accountOperarionDTO.getAccountId(),
				accountOperarionDTO.getAmount(), accountOperarionDTO.getDescription());
		return accountOperarionDTO;
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/operations/Transfers")
	public void transfers(@RequestParam(name = "idSource") String idSource,
			@RequestParam(name = "idDestination") String idDestination, @RequestParam(name = "amount") double amount,
			@RequestParam(name = "description") String description)
			throws BankAccountNotFound, BalanceNotSufficientException {
		this.bankAccountServiceImplementation.transfer(idSource, idDestination, amount, description);

	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/accounts/debit")
	public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFound, BalanceNotSufficientException {
		System.out.println(debitDTO.toString());
		this.bankAccountServiceImplementation.debit(debitDTO.getAccountId(), debitDTO.getAmount(),
				debitDTO.getDescription(),debitDTO.getUpiId());
		return debitDTO;
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/accounts/credit")
	public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFound {
		this.bankAccountServiceImplementation.credit(creditDTO.getAccountId(), creditDTO.getAmount(),
				creditDTO.getDescription());
		return creditDTO;
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PostMapping("/accounts/transfer")
	public void transfer(@RequestBody TransferRequestDTO transferRequestDTO)
			throws BankAccountNotFound, BalanceNotSufficientException {
		this.bankAccountServiceImplementation.transfer(transferRequestDTO.getAccountSource(),
				transferRequestDTO.getAccountDestination(), transferRequestDTO.getAmount(),
				transferRequestDTO.getDescription());
	}

	@PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
	@PutMapping("/accounts/{accountId}")
	public BankAccountDTO updateAccount(@PathVariable String accountId, @RequestBody BankAccountDTO bankAccountDTO) {
		bankAccountDTO.setId(accountId);
		return bankAccountServiceImplementation.updateBankAccount(bankAccountDTO);
	}

	
	@GetMapping("/statement")
	public ResponseEntity<?> generateBankStatement(
			@RequestParam String accountNumber,HttpServletRequest request)
			throws FileNotFoundException, ParseException, DocumentException {
		  System.out.println(request.getHeader("Authorization"));
		  bankStatement.generateStatement(accountNumber);
		  return ResponseEntity.status(200).body(null);
				  
		
	}

	@PostAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/withdrawreqs")
	public List<DebitDTO> withdrawReqs(){
			
		return BankAccountServiceImplementation.debList;
	}

}
