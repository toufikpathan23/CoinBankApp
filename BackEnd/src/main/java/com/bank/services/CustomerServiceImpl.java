package com.bank.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.dtos.ChangePasswordReqDto;
import com.bank.dtos.ChangePasswordResDto;
import com.bank.security.entities.AppUser;
import com.bank.security.repositories.AppUserRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public ChangePasswordResDto changePass(ChangePasswordReqDto changePasswordReqDto) {
		// TODO Auto-generated method stub
	
		String userName=changePasswordReqDto.getName();
		AppUser user=appUserRepository.findAppUserByUsername(userName);
		
		String pw = changePasswordReqDto.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        
        appUserRepository.save(user);
        
        ChangePasswordResDto changePasswordResDto=new ChangePasswordResDto();
        
        changePasswordResDto.setMessage("Your password changed successfully");
        
		return changePasswordResDto;
	}

}
