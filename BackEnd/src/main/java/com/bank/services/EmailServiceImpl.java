package com.bank.services;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bank.dtos.EmailDetails;
import com.bank.dtos.OTPDto;
import com.bank.dtos.OTPRequestDto;
import com.bank.utils.AccountUtils;

import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
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
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.submit(() -> {
			EmailDetails emailDetails=EmailDetails.builder()
	        		.recipient(otpreqDto.getEmail())
	        		.subject("Authentication OTP")
	        		.messageBody("Your OTP for Authentication:"+otp)
	        		.build();
	        
	        sendEmailAlert(emailDetails);
		  // Send the email notification
		});
		
		executorService.shutdown();
        OTPDto odto=new OTPDto();
        odto.setOtp(otp);
		return odto;
	}

	@Override
	public void sendEmailwithAttachment(EmailDetails emailDetails) {
		// TODO Auto-generated method stub
		
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			
			FileSystemResource file=new FileSystemResource(new File(emailDetails.getAttachment()));
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
			
			javaMailSender.send(mimeMessage);
			
			log.info(file.getFilename()+" has been sent to user with email "+emailDetails.getRecipient());
		}
		catch(MessagingException e) 
		{
			throw new RuntimeException(e);
		}
		
	}

}
