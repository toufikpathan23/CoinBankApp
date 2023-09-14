import axios from 'axios';
import { apiUrl } from '../common/constant';


const token = localStorage.getItem('JWT_TOKEN');
const headers = { Authorization: `Bearer ${token}` };

class CustomerService {
  constructor() {
    this.http = axios.create({
      baseURL: apiUrl,
    });
  }

  
  async getCustomer() {
    try {
      const response = await this.http.get('/customers',{headers});
      return response.data;
    } catch (error) {
      console.error(error);
     // throw error;
    }
  }

  async getOneCustomer(id) {
    try {
      const response = await this.http.get(`/customers/${id}`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
      //throw error;
    }
  }

  async getIdCustomerByName(name) {
    try {
      console.log(headers);
      const response = await this.http.get(`/customers/name/${name}`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
     // throw error;
    }
  }

  async searchCustomers(keyword, page) {
    try {
      const response = await this.http.get(`/customers/search?keyword=${keyword}&page=${page}`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
      //throw error;
    }
  }

  async searchAccountByCustomer(page) {
    try {
      const response = await this.http.get(`/Account/searchAccount?page=${page}`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
     // throw error;
    }
  }

  async saveCustomer(customer) {
    try {
      const response = await this.http.post('/customers', customer);
      return response.data;
    } catch (error) {
      console.error(error);
     // throw error;
    }
  }

  async deleteCustomer(id) {
    try {
      await this.http.delete(`/customers/${id}`,{headers});
    } catch (error) {
      console.error(error);
      //throw error;
    }
  }

  async updateCustomer(customer) {
    try {
      const response = await this.http.put(`/customers/${customer.id}`, customer,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
      //throw error;
    }
  }

  async getCustomerById(id) {
    try {
      const response = await this.http.get(`/customers/${id}`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
      //throw error;
    }
  }

  async getAccountsOfCustomer(id) {
    try {
      const response = await this.http.get(`/customers/${id}/accounts`,{headers});
      return response.data;
    } catch (error) {
      console.error(error);
      
    }
  }


  async getwithdrawreqs(){
    try{
      const response = await this.http.get(`/withdrawreqs`,{headers});
      return response.data;

    }catch(error){
      console.log(error)
    }
  }
}

export default CustomerService;
