import React from 'react';
import axios from 'axios';
import { apiUrl } from '../common/constant';



const token = localStorage.getItem('JWT_TOKEN');
const headers = { Authorization: `Bearer ${token}` };

class AccountsService {
  getAccount(accountId, page, size) {
    return axios.get(`${apiUrl}/accounts/${accountId}/pageOperations?page=${page}&size=${size}`,{headers})
      .then(response => response.data)
      .catch(error => {
        console.log(error);
      });
  }

  makeDebit(accountId, amount, description,upiId) {
    const data = { accountId, amount, description,upiId };
    return axios.post(`${apiUrl}/accounts/debit`, data,{headers})
      .catch(error => {
        console.log(error);
      });
  }

  makeCredit(accountId, amount, description) {
    const data = { accountId, amount, description };
    return axios.post(`${apiUrl}/accounts/credit`, data,{headers})
      .catch(error => {
        console.log(error);
      });
  }

  makeTransfer(accountDestination, accountSource, amount, description) {
    const data = { accountSource, accountDestination, amount, description };
    return axios.post(`${apiUrl}/accounts/transfer`, data,{headers})
      .catch(error => {
        console.log(error);
      });
  }

  updateAccount(bankAccount) {
    return axios.put(`${apiUrl}/accounts/${bankAccount.id}`, bankAccount,{headers})
      .then(response => response.data)
      .catch(error => {
        console.log(error);
      });
  }
}

export default  AccountsService;
