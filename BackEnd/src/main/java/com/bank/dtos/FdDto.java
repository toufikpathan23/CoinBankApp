package com.bank.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FdDto {
	
	private String accountId ;
    private double amount ;
    private String description ;
    private LocalDate createdDate=LocalDate.now();
    private double interestRate=7.00;
    

}
