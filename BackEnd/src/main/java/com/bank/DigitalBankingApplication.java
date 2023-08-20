package com.bank;


import com.bank.dtos.*;
import com.bank.entities.AccountOperation;
import com.bank.entities.CurrentAccount;
import com.bank.entities.SavingAccount;
import com.bank.enums.AccountStatus;
import com.bank.enums.OperationType;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.bank.repositories.CustomerRepository;
import com.bank.security.entities.AppRole;
import com.bank.security.entities.AppUser;
import com.bank.security.services.AccountService;
import com.bank.services.BankAccountService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;


@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, AccountService accountService) {
        return args -> {

            accountService.addNewRole(new AppRole(null, "ADMIN"));
            accountService.addNewRole(new AppRole(null, "CUSTOMER"));

//            accountService.addNewUser(new AppUser(null, "Yassine", "1234", new ArrayList<>()));
            accountService.addNewUser(new AppUser(null, "admin", "1234", new ArrayList<>()));
//            accountService.addNewUser(new AppUser(null, "Hafsa", "1234", new ArrayList<>()));
//            accountService.addNewUser(new AppUser(null, "Moussa", "1234", new ArrayList<>()));

//            accountService.addRoleToUser("Yassine", "CUSTOMER");
            accountService.addRoleToUser("admin", "ADMIN");
//            accountService.addRoleToUser("Hafsa", "CUSTOMER");
//            accountService.addRoleToUser("Moussa", "CUSTOMER");

            Stream.of("Amit", "Shubham", "Mandar").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                try {
                    bankAccountService.saveCustomer(customer);
                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
