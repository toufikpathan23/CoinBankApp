package com.bank.services;



import org.springframework.stereotype.Service;

import com.bank.dtos.ChangePasswordReqDto;
import com.bank.dtos.ChangePasswordResDto;
import com.bank.entities.Customer;

@Service
public interface CustomerService {

	ChangePasswordResDto changePass(ChangePasswordReqDto changePasswordReqDto);
}
