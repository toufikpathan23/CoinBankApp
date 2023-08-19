package com.bank.dtos;

import lombok.Data;

import java.util.List;

@Data

public class CustomersDTO {

    List<CustomerDTO> customerDTO;
    int totalpage ;
}

