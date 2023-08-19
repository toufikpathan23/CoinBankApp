package com.musdon.bankapp.repository;

import com.musdon.bankapp.dto.Response;
import com.musdon.bankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
