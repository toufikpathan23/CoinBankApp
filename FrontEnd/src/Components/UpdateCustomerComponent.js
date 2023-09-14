import React, { useState, useEffect } from "react";

import { useParams, useLocation, Navigate, useNavigate } from "react-router-dom";
import CustomerService from "../services/customer.service";

const UpdateCustomerComponent = () => {
  const [customer, setCustomer] = useState({ name: "", email: "" });
  const { id } = useParams();
const location=useLocation();
const navigate=useNavigate();
  
  const cust = { name: customer.name, email: customer.email, id: id };
  useEffect(() => {
    getCustomer();
  }, []);
  const customerService = new CustomerService();
  const getCustomer = async () => {
    try {
      const response = await customerService.getCustomerById(id);

      setCustomer(response);
    } catch (error) {
      console.error("Error fetching customer:", error);
    }
  };

  const updateCustomer = async (event) => {
    event.preventDefault();
    
   
    try {
      await customerService.updateCustomer(cust);
      if(location.state){navigate(`/customer-accounts/${location.state}`);
  return;}
      window.location.href = "/customers";
    } catch (error) {
      console.error("Error updating customer:", error);
    }
  };

  return (
    <div className="page">
      <div className="container">
        <div style={{ textAlign: "center" }} className="card col-md-5 offset-3">
          <div className="card-header" style={{ fontSize: 40 }}>
            Update Customer Profile
          </div>
          <div className="card-body" style={{ fontSize: 30 }}>
            <form onSubmit={updateCustomer}>
              <div className="mb-3">
                <div className="mb-3">
                  <label className="form-label">Name</label>
                  <input
                    type="text"
                    name="name"
                    value={customer.name}
                    onChange={(event) =>
                      setCustomer({ ...customer, name: event.target.value })
                    }
                    className="form-control"
                    style={{ fontSize: 28 }}
                  />
                </div>
                <label className="form-label">Email</label>
                <input
                  type="text"
                  name="email"
                  value={customer.email}
                  onChange={(event) =>
                    setCustomer({ ...customer, email: event.target.value })
                  }
                  className="form-control"
                  style={{ fontSize: 28 }}
                />
              </div>
              <button
                disabled={!customer.name || !customer.email}
                className="btn btn-success mt-3"
                style={{ fontSize: 30 }}
                onClick={updateCustomer}
              >
                Save
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdateCustomerComponent;
