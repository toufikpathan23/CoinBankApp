package com.bank.utils;

import java.time.Year;

public class AccountUtils {

	public static final String ACCOUNT_EXISTS_CODE="001";
	public static final String ACCOUNT_EXISTS_MESSAGE="User already exists with this email...";
	
	public static final String ACCOUNT_CREATION_SUCCESS="002";
	
	public static final String ACCOUNT_CREATION_MESSAGE="Account created successfully....";
	
	public static String generateAccountNumber() {
	//start:2023 + any random six digits
		
		Year currentYear=Year.now();
		
		//min six digit num
		int min=100000;
		
		//max six digit num
		int max=999999;
		
		//generate random number between  min and max
		int randomNumber=(int)Math.floor(Math.random()*(max-min+1));
		
		String year=String.valueOf(currentYear);
		
		String randNum=String.valueOf(randomNumber);
		
		return year+randNum;
		
	}
	
	public static String generateOTP()
	{
		int min=1000;
		
		int max=9999;
		
		int randomNumber=(int)Math.floor(Math.random()*(max-min+1))+min;
		
		return String.valueOf(randomNumber);
	} 
}

