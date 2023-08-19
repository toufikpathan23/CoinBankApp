package com.bank.dtos;


import lombok.Data;

import java.util.Date;

import com.bank.enums.AccountStatus;

@Data
public class BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
    private String type ;
    private double interestRate ;

}
