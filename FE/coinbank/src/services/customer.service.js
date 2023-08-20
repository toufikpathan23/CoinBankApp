import axios from 'axios';

const apiUrl = "http://localhost:9000"

class CustomerService {
  constructor() {
    this.http = axios.create({
      baseURL: apiUrl,
    });
  }

  async getCustomer() {
    try {
      const response = await this.http.get('/customers');
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async getOneCustomer(id) {
    try {
      const response = await this.http.get(`/customers/${id}`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async getIdCustomerByName(name) {
    try {
      const response = await this.http.get(`/customers/name/${name}`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async searchCustomers(keyword, page) {
    try {
      const response = await this.http.get(`/customers/search?keyword=${keyword}&page=${page}`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async searchAccountByCustomer(page) {
    try {
      const response = await this.http.get(`/Account/searchAccount?page=${page}`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async saveCustomer(customer) {
    try {
      const response = await this.http.post('/customers', customer);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async deleteCustomer(id) {
    try {
      await this.http.delete(`/customers/${id}`);
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async updateCustomer(customer) {
    try {
      const response = await this.http.put(`/customers/${customer.id}`, customer);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async getCustomerById(id) {
    try {
      const response = await this.http.get(`/customers/${id}`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }

  async getAccountsOfCustomer(id) {
    try {
      const response = await this.http.get(`/customers/${id}/accounts`);
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }
}

export default CustomerService;
