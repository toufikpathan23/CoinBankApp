import React, { useState, useEffect } from 'react';
import { useParams,useNavigate,useLocation } from 'react-router-dom';
import { Container, Table, Alert, Spinner } from 'react-bootstrap';
import CustomerService from '../services/customer.service';
import Navbar from '../component/Navbar';
const CustomerAccountsComponent = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const customerService = new CustomerService();
  const location = useLocation();
  const [customer, setCustomer] = useState({});
  const [customer1, setCustomer1] = useState({});
  const [accounts, setAccounts] = useState([]);
  const [roles, setRoles] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log(id);
    async function fetchData() {
      try {
        const customerData = await customerService.getOneCustomer(parseInt(id));
        setCustomer(customerData);
        setCustomer1(customerData);

        const accountsData = await customerService.getAccountsOfCustomer(parseInt(id));
        setAccounts(accountsData);

        const storedRoles = localStorage.getItem('ROLES');
        setRoles(storedRoles);

        setLoading(false);
      } catch (error) {
        console.log(error);
        setErrorMessage('Error fetching data');
        setLoading(false);
      }
    }

    fetchData();
  }, []);

  const viewOperations = (account) => {
    navigate(`/one-account/${account.id}`, {state:id});
  };

const updateProfile=(account)=>{
  navigate(`/update-customer/${id}`, {state:id});
}
  function getFirstWord(inputString) {
    const match = inputString.match(/^\w+/);
    return match ? match[0] : null;
  }

  const capitalizeFirstLetter= (inputString )=>{ return inputString.charAt(0).toUpperCase() + inputString.slice(1)};



  return (
    
    <Container className="background-container">
      
      {loading ? (
        <Spinner animation="border" variant="primary" />
      ) : errorMessage ? (
        <Alert variant="danger">{errorMessage}</Alert>
      ) : (
        <>
          <div style={{ textAlign: 'center' }} className="card">
            <div className="card-header">
              <h2 style={{ fontSize: '40px' }}></h2>
              <Table className="table" style={{ fontSize: '20px' }}>
                <thead>
                  <tr>
                    <th>Name :</th>
                    <th>{customer.name}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <th>Email :</th>
                    <th>{customer.email}</th>
                  </tr>
                </tbody>
              </Table>
              <button className="view-profile-button" onClick={updateProfile} >Update Profile</button>
            </div>
            <div className="card-body" style={{ fontSize: '18px' }}>
              <h2 style={{ fontSize: '40px' }}>Accounts</h2>
              <Table className="table">
                <thead>
                  <tr>
                    <th>Account Id</th>
                    {(customer1.id === customer.id || (roles !== null && roles.includes('ADMIN'))) && <th>Balance</th>}
                    <th>Type</th>
                    {(customer1.id === customer.id || (roles !== null && roles.includes('ADMIN'))) && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {accounts.map((account) => (
                    <tr key={account.id}>
                      <td>{account.id}</td>
                      {(customer1.id === customer.id || (roles !== null && roles.includes('ADMIN'))) && (
                        <td>{account.balance} Rs</td>
                      )}
                      <td>{capitalizeFirstLetter(getFirstWord(account.type))}</td>
                      {(customer1.id === customer.id || (roles !== null && roles.includes('ADMIN'))) && (
                        <td>
                          <button onClick={() => viewOperations(account)} className="btn btn-warning">
                            View All Operations
                          </button>
                        </td>
                      )}
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          </div>
        </>
      )}
    </Container>
  );
};

export default CustomerAccountsComponent;
