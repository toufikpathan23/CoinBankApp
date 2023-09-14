import React, { useState, useEffect } from "react";
import { useParams,useNavigate,useLocation } from "react-router-dom";
import AccountsService from "../services/accounts.service";
import "./OneAccountComponent.css";
import { Form, FormGroup, Button } from "react-bootstrap";
import axios from "axios";
import { apiUrl } from "../common/constant";


const OneAccountComponent = () => {
  const [accountDetails, setAccountDetails] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedTransaction, setSelectedTransaction] = useState("debit");
  const [showAccountIdInput, setShowAccountIdInput] = useState(false);
  const [showUpiIdInput, setShowUpiIdInput] = useState(false);
  const [accountId, setAccountId] = useState("");
  const [upiId, setupiId] = useState("");
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const { id } = useParams();
  const AccountService = new AccountsService();
const navigate=useNavigate();
const location = useLocation();
const token = localStorage.getItem('JWT_TOKEN');
const headers = { Authorization: `Bearer ${token}` };
  useEffect(() => {
    setTimeout(()=>{},300);
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await axios.get(
        `${apiUrl}/accounts/${id}/pageOperations`,{headers}
      );
      setAccountDetails(response.data);
    } catch (error) {
      setErrorMessage("Error fetching data");
    }
  };
 
  
    const handleSendStatement = async () => {
      const accountNumber = accountDetails.accountId; 
      try {
        const Response = await axios.get(`${apiUrl}/statement?accountNumber=${accountNumber}`);
        
  
      if (Response) {
        alert('Email sent successfully:', Response);
      }
    }
  catch(err){console.log(err)}};
  
   const gotoWallet=()=>{
    navigate(`/customer-accounts/${accountDetails.customerId}`)
   }


  
  const handleTransactionChange = (e) => {
    setSelectedTransaction(e.target.value);
    setShowAccountIdInput(e.target.value === "transfer");
    setShowUpiIdInput(e.target.value=== "debit")
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleAccountOperation = (e) => {
    e.preventDefault();
    if(amount>100000){
      alert("transaction limit hit");
      window.location.reload();
    }
    else{ if (selectedTransaction === "debit") {
      console.log("in debit");
      AccountService.makeDebit(id, amount, description,upiId);
    } else if (selectedTransaction === "credit") {
      navigate('/payment',{state: {amount:amount,id:id,description:description}});
      //AccountService.makeCredit(id, amount, description);
    } else if (selectedTransaction === "transfer") {
      AccountService.makeTransfer(accountId, id, amount, description);
    }}
   

    setTimeout(() => {
      fetchData();
    }, 1000);
  };

  const buttonContainerStyle = {
    display: 'flex',
    justifyContent: 'flex-end',
    marginTop: '20px',
  };

  const buttonStyle = {
    marginRight: '10px',
    padding: '10px 20px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  };

  const fdMethod=()=>{
    navigate(`/fdsection/${id}`)
  }

  const loanMethod=()=>{
    navigate(`/loansection/${id}`)
  }

  const handlesubmit=()=>{
   
  }

  return (
    <div>
      
      <button onClick={gotoWallet} className="btn btn-primary mx-auto d-block">Go To Wallet</button>
      <div style={buttonContainerStyle}>
      <button type='button '  style={buttonStyle} onClick={fdMethod}>FD Section</button> 
      <button type='button' onClick={loanMethod} style={buttonStyle}>Loan Section</button>
    </div>
      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
      {accountDetails && (
        <div>
          <h2>Account ID: {accountDetails.accountId}</h2>
          <h2>Balance: {accountDetails.balance}.00 Rs</h2>

          <form className="operation-form" onSubmit={handleAccountOperation}>
            <div className="form-group">
              <label className="form-label">Transaction Type</label>
              <div className="radio-group">
                <label className="radio-label">
                  <input
                    type="radio"
                    value="debit"
                    checked={selectedTransaction === "debit"}
                    onChange={handleTransactionChange}
                  />
                  WithDraw
                </label>
                <label className="radio-label">
                  <input
                    type="radio"
                    value="credit"
                    checked={selectedTransaction === "credit"}
                    onChange={handleTransactionChange}
                    
                  />
                  Add To Wallet
                </label>
                <label className="radio-label">
                  <input
                    type="radio"
                    value="transfer"
                    checked={selectedTransaction === "transfer"}
                    onChange={handleTransactionChange}
                  />
                  Transfer
                </label>
              </div>
            </div>
            {showAccountIdInput && (
              <div className="form-group">
                <label className="form-label">Account ID</label>
                <input
                  id="accountId"
                  className="form-input"
                  type="text"
                  value={accountId}
                  onChange={(e) => setAccountId(e.target.value)}
                />
              </div>

            )}
            <div className="form-group">
              <label className="form-label">Amount</label>
              <input
                id="amount"
                className="form-input"
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
              />
            </div>
            {showUpiIdInput && (
            <div className="form-group">
                <label className="form-label">Upi ID</label>
                <input
                  id="upiId"
                  className="form-input"
                  type="text"
                  value={upiId}
                  onChange={(e) => setupiId(e.target.value)}
                />
              </div>
            )}
            <div className="form-group">
              <label className="form-label">Description</label>
              <input
                id="description"
                className="form-input"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
            <button className="submit-button" type="submit" onClick={handlesubmit}>
              Submit
            </button>
          </form>
          <h3>Transaction History</h3><span> <button className="btn btn-primary mx-auto d-block" onClick={handleSendStatement}>Send Statement by Email</button></span>
       
      
         
          <table className="transaction-table">
            <thead>
              <tr><th>Transaction Id</th>
                <th>Date</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {accountDetails.accountOperationDTOList.map((op, index) => (
                <tr key={index}><td>{op.id}</td>
                  <td>{op.operationDate}</td>
                  <td>{op.type}</td>
                  <td>{op.amount}</td>
                  <td>{op.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default OneAccountComponent;
