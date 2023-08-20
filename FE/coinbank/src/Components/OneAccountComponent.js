


import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import AccountsService from '../services/accounts.service';

import { Form, FormGroup, Button } from 'react-bootstrap';
import axios from 'axios';

const apiUrl = "http://localhost:9000"
const OneAccountComponent = () => {
  const [accountDetails, setAccountDetails] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [selectedTransaction, setSelectedTransaction] = useState('debit');
  const [showAccountIdInput, setShowAccountIdInput] = useState(false);
  const [accountId, setAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [description, setDescription] = useState('');
  const { id } = useParams();
  const AccountService = new AccountsService();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await axios.get(`${apiUrl}/accounts/${id}/pageOperations`);
      setAccountDetails(response.data);
    } catch (error) {
      setErrorMessage("Error fetching data");
    }
  };

  const handleTransactionChange = (e) => {
    setSelectedTransaction(e.target.value);
    setShowAccountIdInput(e.target.value === 'transfer');
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleAccountOperation = (e) => {
    e.preventDefault();

   
    if (selectedTransaction === 'debit') {
      console.log("in debit")
      AccountService.makeDebit(id, amount, description);
    } else if (selectedTransaction === 'credit') {
       AccountService.makeCredit(id, amount, description);
    } else if (selectedTransaction === 'transfer') {
       AccountService.makeTransfer(accountId, id, amount, description);
    }
    fetchData();

  };

  return (
    <div>
      <nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="#">Navbar</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarNav">
    <ul class="navbar-nav">
      <li class="nav-item active">
        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Features</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Pricing</a>
      </li>
      <li class="nav-item">
        <a class="nav-link disabled" href="#">Disabled</a>
      </li>
    </ul>
  </div>
</nav>
      <h1>Account Operations</h1>
      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
      {accountDetails && (
        <div>
          <h2>Account ID: {accountDetails.accountId}</h2>
          <h2>Balance: {accountDetails.balance}</h2>

          <form onSubmit={handleAccountOperation}>
            <FormGroup>
              <Form.Label>Transaction Type</Form.Label>
              <div>
                <label>
                  <input
                    type="radio"
                    value="debit"
                    checked={selectedTransaction === 'debit'}
                    onChange={handleTransactionChange}
                  />
                  Debit
                </label>
                <label>
                  <input
                    type="radio"
                    value="credit"
                    checked={selectedTransaction === 'credit'}
                    onChange={handleTransactionChange}
                  />
                  Credit
                </label>
                <label>
                  <input
                    type="radio"
                    value="transfer"
                    checked={selectedTransaction === 'transfer'}
                    onChange={handleTransactionChange}
                  />
                  Transfer
                </label>
              </div>
            </FormGroup>
            {showAccountIdInput && (
          <FormGroup>
            <Form.Label>Account ID</Form.Label>
            <input
              id="accountId"
              type="text"
              value={accountId}
              onChange={(e) => setAccountId(e.target.value)}
            />
          </FormGroup>
        )}

        <FormGroup>
          <Form.Label>Amount</Form.Label>
          <input
            id="amount"
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
        </FormGroup>
        <FormGroup>
          <Form.Label>Description</Form.Label>
          <input
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </FormGroup>
        <Button type="submit">Save Operation</Button>
      </form>

          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {accountDetails.accountOperationDTOList.map((op, index) => (
                <tr key={index}>
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