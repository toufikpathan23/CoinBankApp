package com.bank.services;

//import com.bank.controllers.OTPDto;
import com.bank.dtos.EmailDetails;
import com.bank.dtos.OTPDto;
import com.bank.dtos.OTPRequestDto;

public interface EmailService {
	
	void sendEmailAlert(EmailDetails emailDetails);
	void sendEmailwithAttachment(EmailDetails emailDetails);

	OTPDto getOTP(OTPRequestDto otpreqDto);

}
