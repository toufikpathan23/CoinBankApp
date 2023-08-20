package com.bank.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.security.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);
    
}
