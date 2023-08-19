package com.bank.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.security.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findAppRoleByRoleName(String username);

}
