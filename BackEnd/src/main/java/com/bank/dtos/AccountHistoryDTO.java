package com.bank.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {

    public  String accountId;
    public long customerId;
    private double balance ;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    List<AccountOperationDTO> accountOperationDTOList;
}
