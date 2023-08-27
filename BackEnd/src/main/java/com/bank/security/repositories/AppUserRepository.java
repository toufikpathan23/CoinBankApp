package com.bank.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.security.entities.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);
    
}
