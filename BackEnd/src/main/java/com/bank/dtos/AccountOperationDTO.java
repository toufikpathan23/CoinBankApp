package com.bank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.bank.enums.OperationType;


@Data

@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationDTO {
	
    private Long id ;
    private LocalDateTime operationDate;
    private double amount;
    private OperationType type;
    private String description;
    private String accountId;

}
