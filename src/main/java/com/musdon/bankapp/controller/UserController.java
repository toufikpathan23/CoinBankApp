package com.musdon.bankapp.controller;

import com.musdon.bankapp.dto.Response;
import com.musdon.bankapp.dto.UserRequest;
import com.musdon.bankapp.service.UserService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public Response registerUser(@RequestBody UserRequest userRequest){
        return userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> allRegisteredUsers(){
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public Response fetchUser(@PathVariable(name = "userId") Long userId) throws Exception {
        return userService.fetchUser(userId);
    }

    @GetMapping("/balEnquiry")
    public Response balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response nameEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.nameEnquiry(accountNumber);
    }



}
