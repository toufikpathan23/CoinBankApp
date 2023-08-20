package com.bank.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entities.BankAccount;
import com.bank.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    Page<BankAccount> findAll(Pageable pageable);
    List<BankAccount> findByCustomer(Customer customer);
    Optional<BankAccount> findById(String accounNumber);
}
