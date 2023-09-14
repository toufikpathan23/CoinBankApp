import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useDispatch } from 'react-redux';
import { setCurrentUser } from "../store/action/user.action";
import AuthService from "../services/auth.service";
import CustomerService from "../services/customer.service";
//import  Customer  from '../models/customer.model';
import './LoginPageComponent.css';
//import { GoogleLogin } from "react-google-login";
const LoginPageComponent = () => {

  const cid="495215441570-0qrcj5v223fc8f59ubsvvlnmbdkjjvqb.apps.googleusercontent.com"
  const navigate = useNavigate();
  const authService = AuthService();
  const customerService = new CustomerService();
  const dispatch = useDispatch();

  const [Customer, setCustomer] = useState({
    id: 0,
    name: "",
    email: "",
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = async (data) => {
    const success = await authService.login({
      username: data.username,
      password: data.password,
    });

    if (success) {
      document.getElementById('validcred').innerHTML='';
      const roles = localStorage.getItem("ROLES");
      console.log(roles);
      if (roles && roles.includes("ADMIN")) {
        //sessionStorage.setItem("CUSTOMER",user);
        navigate("/customers");
        window.location.reload();
      } else {
        try {
          console.log(data.username);
          const customerData = await customerService.getIdCustomerByName(
            data.username
          );
          console.log(customerData.id + customerData.email + "asdasda");
          setCustomer(customerData);
          const user = { id: customerData.id, email: customerData.email };
          sessionStorage.setItem("CUSTOMER",user);
          dispatch(setCurrentUser(user));
          navigate(`/customer-accounts/${customerData.id}`);
          setTimeout(() => {
            window.location.reload();
          }, 1000);
        } catch (error) {
          console.log(error);
        }
      }
    }
    else{
      document.getElementById('validcred').innerHTML='Invalid Credential!';
      navigate('/login')
    }
  };

  const forgotPass=()=>{
    navigate("/forgotPass");
  }
  const signup = () => {
    navigate("/signup");
  };

  return (
    
    <div style={{ textAlign: "center" }} className="background-container">
      
      <div className="page">
        <div className="container">
          <div className="d-flex justify-content-center h-100">
            <div className="card">
              <div className="card-header">
                <h3>Sign In</h3>
              </div><span class='validcred' id='validcred' style={{color:'red'}}></span>
              <div className="card-body">
                <form onSubmit={handleSubmit(onSubmit)}>
                  <div className="input-group form-group">
                    <div className="input-group-prepend">
                      <span className="input-group-text">
                        <i className="bi bi-person"></i>
                      </span>
                    </div>
                    <input
                      required
                      type="text"
                      className={`form-control ${
                        errors.username ? "is-invalid" : ""
                      }`}
                      placeholder="username"
                      {...register("username")}
                      autoComplete="current-username"
                    />
                  </div>
                  <div className="input-group form-group">
                    <div className="input-group-prepend">
                      <span className="input-group-text">
                        <i className="bi bi-key"></i>
                      </span>
                    </div>
                    <input
                      required
                      type="password"
                      className={`form-control ${
                        errors.password ? "is-invalid" : ""
                      }`}
                      placeholder="password"
                      
                      {...register("password")}
                      autoComplete="current-password"
                    />
                  </div>
                  <div className="form-group">
                    <input
                      type="submit"
                      disabled={Object.keys(errors).length > 0}
                      value="Login"
                      className={`btn float-right login_btn `}
                    /> 
                    
                    <button type="button" onClick={forgotPass} id="forgotpass">
                    Forgot password
                  </button>
                  </div>
                  <button type="button" onClick={signup} id="signup">
                    Create Account
                  </button> 
                  {/* <GoogleButton
          style={{ marginTop: 10 }}
          onPress={() => authService.loginWithGoogle()}
        /> */}
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="marquee-container">
    <p class="marquee-text" >C-DAC Project</p>
</div>
    </div>
    
  );
};

export default LoginPageComponent;
