package com.bank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bank.dtos.EmailDetails;
import com.bank.dtos.OTPDto;
import com.bank.dtos.OTPRequestDto;
import com.bank.utils.AccountUtils;



@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		// TODO Auto-generated method stub
		
		try {
			SimpleMailMessage mailMessage=new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
			System.out.println("Mail sent successfully...");
		}
		catch(Exception e) 
		{
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public OTPDto getOTP(OTPRequestDto otpreqDto) {
		// TODO Auto-generated method stub
		String otp=AccountUtils.generateOTP();
		EmailDetails emailDetails=EmailDetails.builder()
        		.recipient(otpreqDto.getEmail())
        		.subject("Verification OTP")
        		.messageBody("OTP for your email verification:"+otp)
        		.build();
        
        sendEmailAlert(emailDetails);
        OTPDto odto=new OTPDto();
        odto.setOtp(otp);
		return odto;
	}

}
