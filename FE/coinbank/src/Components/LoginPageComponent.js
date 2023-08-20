


import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import AuthService from '../services/auth.service';
import CustomerService from '../services/customer.service';
//import  Customer  from '../models/customer.model';

const LoginPageComponent = () => {
  const navigate = useNavigate();
  const authService =AuthService();
  const customerService = new CustomerService();

  const [Customer, setCustomer] = useState({
    id: 0,
    name: "",
    email: ""
  });

  const { register, handleSubmit, formState: { errors } } = useForm();


  
  
  



  const onSubmit = async (data) => {
    
    const success = await authService.login({
      username: data.username,
      password: data.password
    });

    if (success) {
      const roles = localStorage.getItem("ROLES");
      console.log(roles);
      if (roles && roles.includes("ADMIN")) {
        navigate('/customers');
        window.location.reload();
      } else {
        try {console.log(data.username);
          const customerData = await customerService.getIdCustomerByName(data.username);
         console.log(customerData.id+customerData.email+"asdasda");
          setCustomer(customerData);
          navigate(`/customer-accounts/${customerData.id}`);
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        } catch (error) {
          console.log(error);
        }
      }
    }
  };


  const signup=()=>{
    navigate('/signup')

  }

  return (
    <div style={{ textAlign: 'center' }}>
      <div className="page">
        <div className="container">
          <div className="d-flex justify-content-center h-100">
            <div className="card">
              <div className="card-header">
                <h3>Sign In</h3>
              </div>
              <div className="card-body">
                <form onSubmit={handleSubmit(onSubmit)}>
                  <div className="input-group form-group">
                    <div className="input-group-prepend">
                      <span className="input-group-text"><i className="bi bi-person"></i></span>
                    </div>
                    <input
                      type="text"
                      className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                      placeholder="username"
                      {...register('username')}
                    />
                  </div>
                  <div className="input-group form-group">
                    <div className="input-group-prepend">
                      <span className="input-group-text"><i className="bi bi-key"></i></span>
                    </div>
                    <input
                      type="password"
                      className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                      placeholder="password"
                      {...register('password')}
                    />
                  </div>
                  <div className="form-group">
                    <input
                      type="submit"
                      disabled={Object.keys(errors).length > 0}
                      value="Login"
                      className="btn float-right login_btn"
                    />
                  </div>
                  <button type="button" onClick={signup} id="signup">Create Account</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPageComponent;