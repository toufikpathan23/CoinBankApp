package com.bank.services;

import com.bank.dtos.*;
import com.bank.entities.*;
import com.bank.enums.OperationType;
import com.bank.exceptions.BalanceNotSufficientException;
import com.bank.exceptions.BankAccountNotFound;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.mappers.BankAccountMapperImplementation;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.bank.repositories.CustomerRepository;
import com.bank.security.entities.AppUser;
import com.bank.security.services.AccountService;
import com.bank.utils.AccountUtils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BankAccountServiceImplementation implements BankAccountService {

    @Autowired
    @Order(1)
    BankAccountRepository bankAccountRepository;
    @Autowired
    @Order(1)
    CustomerRepository customerRepository;
    @Autowired
    @Order(1)
    AccountOperationRepository accountOperationRepository;

    @Autowired
    BankAccountMapperImplementation dtoMapper;
    @Autowired
    AccountService accountService;
    
    @Autowired
    private EmailService emailService;


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customer = customerRepository.save(customer);
        
        
        EmailDetails emailDetails=EmailDetails.builder()
        		.recipient(customer.getEmail())
        		.subject("ACCOUNT CREATION")
        		.messageBody("congratulation! You account has been created.\nAccount Deatails:\n Name:"+customer.getName()+"\nAccound Id:"+customer.getId()+"\nPassword:"+customer.getName())
        		.build();
        CompletableFuture<Void> sendEmailNotification = CompletableFuture.runAsync(() -> {
        	  // Send the email notification
            emailService.sendEmailAlert(emailDetails);
        	});
        
        sendEmailNotification.join();
        
        AppUser appUser = new AppUser(null, customer.getName(), customer.getName(), new ArrayList<>());
        accountService.addNewUser(appUser);
        accountService.addRoleToUser(customer.getName(), "CUSTOMER");
        saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
        saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        SavingAccount savingAccount = new SavingAccount();
        //savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setId(AccountUtils.generateAccountNumber());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        savingAccount = bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingBankAccount(savingAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(AccountUtils.generateAccountNumber());
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer);
        currentAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(currentAccount);

    }

    public List<BankAccountDTO> bankAccountListOfCustomer(Long id){
        Customer customer = new Customer();
        customer.setId(id);
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomer(customer);
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public List<CustomerDTO> listCustomers(int page) {
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page,6));
        List<CustomerDTO> collect = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Customer> listCustomer() {
        return customerRepository.findAll();
    }




    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFound("Bank account Not Found"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount =(SavingAccount) bankAccount;
            SavingBankAccountDTO savingBankAccountDTO = dtoMapper.fromSavingBankAccount(savingAccount);
            return savingBankAccountDTO ;
        }else{
            CurrentAccount currentAccount =(CurrentAccount) bankAccount;
            CurrentBankAccountDTO currentBankAccountDTO = dtoMapper.fromCurrentBankAccount(currentAccount);
            return currentBankAccountDTO ;
        }
    }

    @Override
    public void debit(String accountId, double amount , String description) throws BalanceNotSufficientException, BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFound("Account not found") );
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);

        accountOperation.setAmount(amount);
        bankAccountRepository.save(bankAccount);
        accountOperationRepository.save(accountOperation);
        
        
        CompletableFuture<Void> sendEmailNotification = CompletableFuture.runAsync(() -> {
        	  // Send the email notification
        	EmailDetails emailDetails=EmailDetails.builder()
            		.recipient(bankAccount.getCustomer().getEmail())
            		.subject("AMOUNT DEBITED")
            		.messageBody(amount+".Rs debited from your account NO:"+bankAccount.getId()+".\n Current Balance:"+bankAccount.getBalance())
            		.build();
            
            emailService.sendEmailAlert(emailDetails);
        	});
        sendEmailNotification.join();


    }


    @Override
    public void credit(String accountId, double amount , String description) throws BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFound("Account not found") );

        if (bankAccount == null)
            throw new BankAccountNotFound("bank Account not found");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setOperationDate(new Date());
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        bankAccountRepository.save(bankAccount);
        accountOperationRepository.save(accountOperation);
        
        EmailDetails emailDetails=EmailDetails.builder()
        		.recipient(bankAccount.getCustomer().getEmail())
        		.subject("AMOUNT CREDITED")
        		.messageBody(amount+".Rs Credited to your account NO:"+bankAccount.getId()+".\n Current Balance:"+bankAccount.getBalance())
        		.build();
        
        emailService.sendEmailAlert(emailDetails);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount,String description) throws BankAccountNotFound, BalanceNotSufficientException {
        AccountOperationDTO accountSourceOperationDTO = new AccountOperationDTO();
        accountSourceOperationDTO.setAccountId(accountIdSource);
        accountSourceOperationDTO.setAmount(amount);
        debit(accountIdSource,amount,description);
        AccountOperationDTO accountDestOperationDTO = new AccountOperationDTO();
        accountDestOperationDTO.setAccountId(accountIdDestination);
        accountDestOperationDTO.setAmount(amount);
        credit(accountIdDestination,amount,description);

    }

    @Override
    public BankAccountsDTO getBankAccountList(int page) {

        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(PageRequest.of(page,5));
        List<BankAccountDTO> bankAccountDTOList = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof  SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }else{
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
                }

        ).collect(Collectors.toList());
        BankAccountsDTO  bankAccountsDTO= new BankAccountsDTO();
        bankAccountsDTO.setBankAccountDTOS(bankAccountDTOList);
        bankAccountsDTO.setTotalPage(bankAccounts.getTotalPages());
        return bankAccountsDTO;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        customer = customerRepository.save(customer);
        AppUser appUser = new AppUser(customer.getId()+1, customer.getName(), customer.getName(), new ArrayList<>());
        accountService.update(appUser);
        accountService.addRoleToUser(customer.getName(), "CUSTOMER");
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount;
        if(bankAccountDTO.getType().equals("saving account")) {
            SavingBankAccountDTO saving = new SavingBankAccountDTO();

             BeanUtils.copyProperties(bankAccountDTO,saving);
            bankAccount = dtoMapper.fromSavingBankAccountDTO(saving);
            bankAccount = bankAccountRepository.save(bankAccount);
            return dtoMapper.fromSavingBankAccount((SavingAccount) bankAccount);

        }else{
            CurrentBankAccountDTO current = new CurrentBankAccountDTO();

            BeanUtils.copyProperties(bankAccountDTO,current);
            bankAccount = dtoMapper.fromCurrentBankAccountDTO(current);
            bankAccount = bankAccountRepository.save(bankAccount);
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }


    }

    @Override
    public CustomerDTO getCustomerByName(String name) {
        Customer customer = customerRepository.getCustomerByName(name);
        return  dtoMapper.fromCustomer(customer);

    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
        accountService.deleteUser(customerId+1);
    }

    @Override
    public List<AccountOperationDTO> accountOperationHistory(String accountId){
        List<AccountOperation>  accountOperations= accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map(accountOperation -> {
           return   dtoMapper.fromAccountOperation(accountOperation);
        }).collect(Collectors.toList());
        return accountOperationDTOS;
    }

    @Override
   public AccountHistoryDTO getAccoutHistory(String accountId, int page, int size) throws BankAccountNotFound {
        BankAccount bankAccount= bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null)
            throw  new BankAccountNotFound("bank not fount ");
        Page<AccountOperation> accountOperationPage = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOList=accountOperationPage.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOList(accountOperationDTOList);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperationPage.getTotalPages());
        return accountHistoryDTO;

   }

    @Override
    public CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException {
        Page<Customer> customers ;

        customers = customerRepository.searchByName(keyword,PageRequest.of(page,5));
        List<CustomerDTO> customerDTOS=customers.getContent().stream().map(c->dtoMapper.fromCustomer(c)).collect(Collectors.toList());
        if (customers == null)
            throw new CustomerNotFoundException("customer not fount");

        CustomersDTO customersDTO= new CustomersDTO();
        customersDTO.setCustomerDTO(customerDTOS);
        customersDTO.setTotalpage(customers.getTotalPages());
        return customersDTO;
    }

}
