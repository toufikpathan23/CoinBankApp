package com.bank.exception_handler;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.custom_exception.ResourceNotFoundException;
import com.bank.dtos.ApiResponse;

//import io.swagger.v3.oas.models.responses.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
@ExceptionHandler(MethodArgumentNotValidException.class)
	
	public ResponseEntity<?> handleMethodArgumentNotValidException 
	(MethodArgumentNotValidException e)
	{
		return ResponseEntity.
				status(HttpStatus.BAD_REQUEST)
				.body(e.getFieldErrors().stream().
						collect(Collectors.toMap(f->f.getField(),f->f.getDefaultMessage())));
		//List<FieldError> getFieldErrors()
		//Api of FieldError class:getField:field name with err,
		//getDefaultMessage:err msg
	}

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<?> handleResourceNotFoundException
(ResourceNotFoundException e)
{
	return ResponseEntity.
			status(HttpStatus.BAD_REQUEST)
			.body(new ApiResponse(e.getMessage()));
}

}
