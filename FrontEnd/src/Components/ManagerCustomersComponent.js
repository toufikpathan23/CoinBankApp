import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form"; // You can use this library for forms in React
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";


import CustomerService from "../services/customer.service";


function ManageCustomersComponent() {
  const [customers, setCustomers] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [customer, setCustomer] = useState(null);
  const [username, setUsername] = useState("");
  const [roles, setRoles] = useState(null);

  const navigate = useNavigate();

  const { handleSubmit, register } = useForm();
  const customerService=new CustomerService();
  useEffect(() => {
   // console.log(localStorage.getItem("username"));
    setRoles(localStorage.getItem("ROLES"));
    setUsername(localStorage.getItem("username"));
    getCustomer();
    searchCustomers();
  }, []);

  const searchCustomers = async () => {
    try {
      var kw = searchKeyword; // Assuming you have a 'searchKeyword' state
     
      
      // Perform the equivalent of Angular's HTTP request for searching customers
      // Replace this with your actual API call, for example using Axios or Fetch
      const response = await customerService.searchCustomers(kw,0);
      console.log(response)
      console.log(typeof response.customerDTO);
      const data =  response.customerDTO;
      console.log(typeof data);
  
      // Update 'customers' state with the fetched data
      setCustomers(data);
  
      // Clear any previous error message
      setErrorMessage(null);
    } catch (error) {
      // Handle errors and set error message
      setErrorMessage(error.message);
    }
  };

  const getCustomer = () => {
    if (username !== "admin") {
      // Perform the equivalent of Angular's HTTP request to get customer data
      // Update 'customer' state and handle errors
    }
  };

  const renderCustomers = () => {
    return (
      <div style={{ textAlign: "center" }} className="card">
        <div className="card-header">
          <h2 style={{ fontSize: "40px" }}>Customers</h2>
        </div>
        {/* Rendering the search form */}
        {/* Rendering the customer table */}
        {/* Rendering pagination */}
      </div>
    );
  };
  const handleDeleteButton = (customer) => {
    Swal.fire({
      title: "Are you sure that you want to delete this customer ?",
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: "Yes",
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
       customerService.deleteCustomer(customer.id)
        searchCustomers();
      }
    });
  };



  const handlewithdrawreqs=()=>{
    navigate('/withdrawreqs')
    

  }
  const handleUpdateButton = (customer) => {
    // Perform the equivalent of Angular's HTTP request to update customer
    // Navigate to '/customers' after updating
    navigate("/customers");
  };

  const goToUpdateCustomer = (customer) => {
    // Perform the equivalent of Angular's HTTP request to update customer
    // Navigate to '/update-customer/:id' with appropriate ID
    navigate(`/update-customer/${customer.id}`);
  };

  const handleCustomerAccounts = (customer) => {
    // Navigate to '/customer-accounts/:id' with appropriate ID
    navigate(`/customer-accounts/${customer.id}`);
  };
  const renderLoading = () => {
    return <div>Loading, please wait...</div>;
  };


  const renderFailureOrLoading = () => {
    return errorMessage ? (
      <div className="text-danger">{errorMessage}</div>
    ) : (
      renderLoading()
    );
  };
  const gotoPage = (page) => {
    setCurrentPage(page);
    searchCustomers();
  };
  const renderCustomerTable = () => {
    if (!customers) return null;

    return (<div>
      <button type="button" onClick={handlewithdrawreqs}>withdraw requests</button>
      <table className="table mt-5">
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>{c.email}</td>
              <td>
                {roles && roles.includes('ADMIN') && (
                  <>
                    <button
                      onClick={() => handleDeleteButton(c)}
                      className="btn btn-danger"
                      style={{ marginRight: "8px", fontSize: "15px" }}
                    >
                      Delete
                    </button>
                    <button
                      onClick={() => goToUpdateCustomer(c)}
                      className="btn btn-warning"
                      style={{ marginRight: "8px", fontSize: "15px" }}
                    >
                      Update
                    </button>
                  </>
                )}
                <button
                  onClick={() => handleCustomerAccounts(c)}
                  className="btn btn-success"
                  style={{ marginRight: "8px", fontSize: "15px" }}
                >
                  View Accounts
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>
    );
  };

  const renderPagination = () => {
    if (!customers) return null;

    return (
      <ul className="nav nav-pills">
        {/* Map over pagination pages and render each button */}
      </ul>
    );
  };
  return (
    <div className="page">
      <div className="container">
        {customers ? (
          <div style={{ textAlign: 'center' }} className="card">
            <div className="card-header">
              <h2 style={{ fontSize: '40px' }}>Customers</h2>
            </div>
            <div
             
              className="card-body" style={{ fontSize: '30px', textAlign: 'center'}}
            >
              <form onSubmit={handleSubmit(searchCustomers)}>
                <div className="input-group">
                  <label className="input-group-text" style={{ fontSize: '30px' }}>
                    Keyword:
                  </label>
                  <input
                    type="text"
                    {...register('keyword')}
                    className="form-control"
                    style={{ fontSize: '28px' }}
                    onChange={(e)=>{setSearchKeyword(e.target.value)}}
                  />
                  <button className="btn btn-warning" style={{ fontSize: '30px' }}>
                    Search
                  </button>
                </div>
              </form>
              {renderCustomerTable()}
              {renderPagination()}
            </div>
          </div>
        ) : (
          <div className="text-danger">
            {errorMessage ? errorMessage : 'Loading, please wait ...'}
          </div>
        )}
      </div>
    </div>
  );
}

export default ManageCustomersComponent;
