import React, { useState } from 'react';
import './LoanComponent.css'; // Import your CSS file for component styling
import AccountsService from "../services/accounts.service";
import { useParams,useNavigate,useLocation } from "react-router-dom";
   
function LoanComponent() {
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');
  const [interestRate, setInterestRate] = useState('');
  const AccountService = new AccountsService();
  const navigate=useNavigate();
const location = useLocation();
const { id } = useParams();
  const handleSubmit = (event) => {
    event.preventDefault();

    const desc="LOAN "+description;
    AccountService.makeCredit(id, amount, desc);
        navigate(`/one-account/${id}`)
    // You can perform further actions here, like submitting the data to an API
    console.log('Loan data submitted:', { amount, description, interestRate });
  };

  return (
    <div className="loan-form-container">
      <h2 className="form-title">Loan Application</h2>
      <form className="loan-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="amount">Amount:</label>
          <input
            type="number"
            id="amount"
            className="form-input"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description:</label>
          <input
            type="text"
            id="description"
            className="form-input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="interestRate">Interest Rate:</label>
          <input
            type="text" readOnly
            id="interestRate"
            className="form-input"
            value={12.00}
            onChange={(e) => setInterestRate(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="submit-button">Submit</button>
      </form>
    </div>
  );
}

export default LoanComponent;
