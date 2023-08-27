package com.bank.controllers;

import com.bank.dtos.BankAccountDTO;
import com.bank.dtos.ChangePasswordReqDto;
import com.bank.dtos.ChangePasswordResDto;
import com.bank.dtos.CustomerDTO;
import com.bank.dtos.CustomersDTO;
import com.bank.dtos.OTPDto;
import com.bank.dtos.OTPRequestDto;
import com.bank.entities.Customer;
import com.bank.exceptions.CustomerNotFoundException;
import com.bank.repositories.CustomerRepository;
import com.bank.services.BankAccountService;
import com.bank.services.CustomerService;
import com.bank.services.EmailService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    @Autowired
    private BankAccountService bankAccountService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @PreAuthorize("permitAll()")
    @GetMapping("/customers/all/{page}")
    public List<CustomerDTO> customers(@PathVariable int page) {
        return bankAccountService.listCustomers(page);
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/customers/{id}/accounts")
    public List<BankAccountDTO> accountsListOfCustomer(@PathVariable(name = "id") Long customerId) {
        return bankAccountService.bankAccountListOfCustomer(customerId);
    }

    @PostAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }
    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @GetMapping("/customers/name/{name}")
    public CustomerDTO getCustomerByname(@PathVariable(name = "name") String name){
        return bankAccountService.getCustomerByName(name);
    }

    @PostAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/customers/search")
    public CustomersDTO getCustomerByName(@RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam(name = "page", defaultValue = "0") int page) throws CustomerNotFoundException {
        CustomersDTO customersDTO = bankAccountService.getCustomerByName("%" + keyword + "%", page);
        return customersDTO;
    }

      
    @PostMapping("/customers/save")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @PostAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER')")
    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        bankAccountService.deleteCustomer(customerId);
    }
    
    
    @PostMapping("/verifyOtp")
    public OTPDto generateOTP(@RequestBody OTPRequestDto otpreqDto) 
    {
    	return emailService.getOTP(otpreqDto);
    }
    
    
    @PostMapping("/changepassword")
    public ChangePasswordResDto changePassword(@RequestBody ChangePasswordReqDto changePasswordReqDto)
    {
    	System.out.println(changePasswordReqDto.toString());
    	if(changePasswordReqDto.getName().equals("abc"))
    	{
    		System.out.println("I am inside here...");
    		Customer cust=customerRepository.findByEmail(changePasswordReqDto.getEmail());
    		System.out.println(cust);
    		System.out.println("Customer"+cust.toString());
    		changePasswordReqDto.setName(cust.getName());
    		
    		return customerService.changePass(changePasswordReqDto);
    	}
    	
    	return customerService.changePass(changePasswordReqDto);
    }
	/*
	 * @PostAuthorize("hasAuthority('ADMIN')or hasAuthority('CUSTOMER')")
	 * 
	 * @PutMapping("/changepass") public ChangePasswordResDto
	 * changePassword(@RequestBody ChangePasswordReqDto reqDto) { return
	 * bankAccountService. }
	 */
    

}
