package com.bank.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bank.entities.AccountOperation;
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
	private static final String FILE="C:\\users\\Rafik PATHAN\\Documents\\MyStatement.pdf";
	/*
	1. retrieve list of transaction within a date range given an account number
	2. generate a pdf file transaction.
	3. send the file via email
	 */
	
	public List<AccountOperation> generateStatement(String accountNumber,String startDate,String endDate) throws ParseException, FileNotFoundException, DocumentException
	{
		Date start=new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
		Date end=new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		List<AccountOperation> operationList=accountOperationRepository.findAll().stream().filter(operation->operation.getBankAccount().getId().equals(accountNumber))
				                             .filter(operation->operation.getOperationDate().after(start))
				                             .filter(operation->operation.getOperationDate().before(end)).collect(Collectors.toList());
		Customer customer=bankAccountRepository.findById(accountNumber).orElseThrow().getCustomer();
		String customerName=customer.getName();
		Rectangle statementSize=new Rectangle(PageSize.A4);
		Document document=new Document(statementSize);
		log.info("setting size of document");
		
		OutputStream outPutStream=new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outPutStream);
		document.open();
		
		PdfPTable bankInfoTable=new PdfPTable(1);
		PdfPCell bankName=new PdfPCell(new Phrase("CoinBank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		PdfPCell bankAdress=new PdfPCell(new Phrase("IACSD,Akhurdi,Pune-413511"));
		bankAdress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAdress);
		
		PdfPTable statementInfo=new PdfPTable(2);
		PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date:"+startDate));
		customerInfo.setBorder(0);
		PdfPCell statement=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate=new PdfPCell(new Phrase("End Date:"+endDate));
		stopDate.setBorder(0);
		PdfPCell name=new PdfPCell(new Phrase("Customer Name"+customerName));
		name.setBorder(0);
		PdfPCell space=new PdfPCell();
		PdfPCell address=new PdfPCell(new Phrase("Customer Adress: Pune"));
		address.setBorder(0);
		
		//PdfPTable 
		
		return operationList;
	}
	
	
}
