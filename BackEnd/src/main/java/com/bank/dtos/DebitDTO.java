package com.bank.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class DebitDTO {

    private String accountId ;
    private double amount ;
    private String description ;
    private String upiId;

}
