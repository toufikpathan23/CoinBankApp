package com.bank.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.dtos.EmailDetails;
import com.bank.entities.AccountOperation;
import com.bank.entities.BankAccount;
import com.bank.entities.Customer;
import com.bank.repositories.AccountOperationRepository;
import com.bank.repositories.BankAccountRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	private AccountOperationRepository accountOperationRepository;
	private BankAccountRepository bankAccountRepository;
	private EmailService emailService;
	
	private static final String FILE="C:\\users\\Rafik PATHAN\\Documents\\MyStatement.pdf";
	/*
	1. retrieve list of transaction within a date range given an account number
	2. generate a pdf file transaction.
	3. send the file via email
	 */
	
	public List<AccountOperation> generateStatement(
			String accountNumber/* ,String startDate,String endDate */) throws ParseException, FileNotFoundException, DocumentException
	{
		/*
		 * Date start=new SimpleDateFormat("yyyy-MM-dd").parse(startDate); Date end=new
		 * SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		 */
		/*
		 * List<AccountOperation> operationList1=accountOperationRepository.findAll();
		 * System.out.println(operationList1);
		 */
		List<AccountOperation> operationList=new ArrayList<AccountOperation>();
		try {
				accountOperationRepository.findAll().stream()
				                             .forEach(operation->
				                                   {
				                                	   
				                                   if(operation.getBankAccount().getId().equals(accountNumber))
				                                	   operationList.add(operation);
				                                   });
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		Customer customer=bankAccountRepository.findById(accountNumber).orElseThrow().getCustomer();
		BankAccount bankAccount=bankAccountRepository.findById(accountNumber).orElseThrow();
		String customerName=customer.getName();
		Rectangle statementSize=new Rectangle(PageSize.A4);
		Document document=new Document(statementSize);
		log.info("setting size of document");
		
		OutputStream outPutStream=new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outPutStream);
		document.open();
		
		PdfPTable bankInfoTable=new PdfPTable(1);
		PdfPCell bankName=new PdfPCell(new Phrase("COINBANK"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		PdfPCell bankAdress=new PdfPCell(new Phrase("IACSD,Akhurdi,Pune-413511\n\n"));
		bankAdress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAdress);
		
		PdfPTable statementInfo=new PdfPTable(3);
		/*
		 * PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date:"+startDate));
		 * customerInfo.setBorder(0);
		 */
		
		/*
		 * PdfPCell stopDate=new PdfPCell(new Phrase("End Date:"+endDate));
		 * stopDate.setBorder(0);
		 */
		PdfPCell name=new PdfPCell(new Phrase("Customer Name: "+customerName));
		name.setBorder(0);
		PdfPCell space=new PdfPCell();
		PdfPCell address=new PdfPCell(new Phrase("Account Number: "+accountNumber));
		address.setBorder(0);
		PdfPCell balance=new PdfPCell(new Phrase("Current Balance: "+bankAccount.getBalance()));
		balance.setBorder(0);
		
        statementInfo.addCell(name);
		statementInfo.addCell(address);
		statementInfo.addCell(balance);
		
		
		PdfPTable heading=new PdfPTable(1);
		
		PdfPCell statement=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		
		heading.addCell(statement);
		
		PdfPTable transactionsTable=new PdfPTable(4); 
		
		PdfPCell transactionId=new PdfPCell(new Phrase("Transction Id"));
		transactionId.setBackgroundColor(BaseColor.BLUE);
		transactionId.setBorder(0);
		PdfPCell date=new PdfPCell(new Phrase("Date"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		
		PdfPCell transactionType=new PdfPCell(new Phrase("Transaction Type"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		
		PdfPCell transactionAmount=new PdfPCell(new Phrase("Transaction Amount"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		
		/*PdfPCell totalBalance=new PdfPCell(new Phrase("Total Balance"));
		totalBalance.setBackgroundColor(BaseColor.BLUE);
		totalBalance.setBorder(0);*/
		
		transactionsTable.addCell(transactionId);
		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(transactionAmount);
		//transactionsTable.addCell(totalBalance);
		
		operationList.forEach(operation->{ 
			
			transactionsTable.addCell(new Phrase(operation.getId().toString()));
			transactionsTable.addCell(new Phrase(operation.getOperationDate().toString()));
			transactionsTable.addCell(new Phrase(operation.getType().toString()));
			transactionsTable.addCell(new Phrase(String.valueOf(operation.getAmount())));
			//transactionsTable.addCell(new Phrase(String.valueOf(operation.getBankAccount().getBalance())));
			
		});
		
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(heading);
		document.add(transactionsTable);
		
		document.close();
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.submit(() -> {
		EmailDetails emailDetails=EmailDetails.builder()
				.recipient(customer.getEmail())
				.subject("Statement of Account")
				.messageBody("Please find below attached Statement of your account!")
				.attachment(FILE)
				.build();
		
		emailService.sendEmailwithAttachment(emailDetails);
		});
		
		executorService.shutdown();
		
		return operationList;
	}
	
	
}
