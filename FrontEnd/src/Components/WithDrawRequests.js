import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form"; // You can use this library for forms in React
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { Link } from 'react-router-dom';

import CustomerService from "../services/customer.service";


function WithDrawRequests() {
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
    const customerService = new CustomerService();
    useEffect(() => {

        getCustomer();

    }, []);



    const getCustomer =async () => {
        const response = await customerService.getwithdrawreqs();
      console.log(response)
     // console.log(typeof response.DebitDTO);
     // const aa =  response.data;
    //   const DebitDTO=data.DebitDTO;
    //   console.log(aa);
  
      // Update 'customers' state with the fetched data
     setCustomers(response);

    };
    return (
        <div>
            <Link to="/customers">Customers</Link>
            
            <table  className="table mt-5">

                <thead>
                    <tr>
                        <th>AccountId</th>
                        <th>Amount</th>
                        <th>Description</th>
                        <th>UpiId</th>
                    </tr>
                </thead>
                <tbody>
                    {customers.map((c) => (
                        <tr key={c.accountId}>
                            <td>{c.accountId}</td>
                            <td>{c.amount}</td>
                            <td>{c.description}</td>
                            <td>{c.upiId}</td>
                        </tr>))}
                </tbody>
            </table>
        </div>
    )

}

export default WithDrawRequests;