package com.musdon.bankapp.service;

import com.musdon.bankapp.dto.*;
import com.musdon.bankapp.email.dto.EmailDetails;
import com.musdon.bankapp.email.service.EmailService;
import com.musdon.bankapp.entity.User;
import com.musdon.bankapp.repository.UserRepository;
import com.musdon.bankapp.utils.ResponseUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, EmailService emailService){
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        /**
         * check if the user exists, if the user doesn't exist return response,
         * generate account number
         * go ahead to save the user
         */


        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_EXISTS_CODE)
                    .responseMessage(ResponseUtils.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();

        }

        User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .otherName(userRequest.getOtherName())
                    .gender(userRequest.getGender())
                    .address(userRequest.getAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.LENGTH_OF_ACCOUNT_NUMBER))
                    .accountBalance(userRequest.getAccountBalance())
                    .email(userRequest.getEmail())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                    .status("ACTIVE")
                    .dateOfBirth(userRequest.getDateOfBirth())
                    .build();



        User savedUser = userRepository.save(user);

        String accountDetails = savedUser.getFirstName() + savedUser.getLastName()
                + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber();
        //Send email alert
        EmailDetails message = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT DETAILS")
                .messageBody("Congratulations! Your account has been successfully created! Kindly find your details below: \n" + accountDetails)
                .build();

        emailService.sendSimpleEmail(message);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();


//        List<Response> response = new ArrayList<>();
//        for (User user : usersList){
//            System.out.println(user.toString());
//            response.add(Response.builder()
//                    .responseCode(ResponseUtils.SUCCESS)
//                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
//                    .data(Data.builder()
//                            .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
//                            .accountNumber(user.getAccountNumber())
//                            .accountBalance(user.getAccountBalance())
//                            .build())
//                    .build());
//        }

        return usersList.stream().map(user -> Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build()).collect(Collectors.toList());


    }

    @Override
    public Response fetchUser(Long userId) {

        if (!userRepository.existsById(userId)){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(userId).get();

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        /**
         * check if accNum exists
         * return the balance info
         */

        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountExist){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountNumber(accountNumber)
                        .build())
                .build();
    }

    @Override
    public Response credit(TransactionRequest transactionRequest) {
        /**
         * add amount to current accountBalance
         */
        User receivingUser = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        if (!userRepository.existsByAccountNumber(transactionRequest.getAccountNumber())){
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }

        receivingUser.getAccountBalance().add(transactionRequest.getAmount());
        userRepository.save(receivingUser);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType("Credit");
        transactionDto.setAccountNumber(transactionRequest.getAccountNumber());
        transactionDto.setAmount(transactionRequest.getAmount());

        transactionService.saveTransaction(transactionDto);


        return Response.builder()
                .responseCode(ResponseUtils.SUCCESSFUL_TRANSACTION)
                .responseMessage(ResponseUtils.ACCOUNT_CREDITED)
                .data(Data.builder()
                        .accountNumber(transactionRequest.getAccountNumber())
                        .accountBalance(receivingUser.getAccountBalance())
                        .accountName(receivingUser.getFirstName() + " " + receivingUser.getLastName() + receivingUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response debit(TransactionRequest transactionRequest) {
        return null;
    }
}
