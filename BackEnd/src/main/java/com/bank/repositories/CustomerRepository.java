package com.bank.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.name like :kw")
    Page<Customer> searchByName(@Param("kw") String keyword, Pageable pageable);

    Page<Customer> findAll(Pageable pageable);
    Customer getCustomerByName(String name);

	Customer findByEmail(String email);

}
