package com.musdon.bankapp.email;

import com.musdon.bankapp.email.dto.EmailDetails;
import com.musdon.bankapp.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/sendMail")
    public String sendEmail(@RequestBody EmailDetails emailDetails){
        return emailService.sendSimpleEmail(emailDetails);
    }

    @PostMapping("/sendMailWithoutAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails emailDetails){
        return emailService.sendEmailWithAttachment(emailDetails);
    }
}
