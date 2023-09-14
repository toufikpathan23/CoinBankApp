



import React, { useState } from 'react';
import './SignUpForm.css';

import axios from 'axios';
import { apiUrl } from '../common/constant';
import { useNavigate } from 'react-router-dom';



const SignupForm = () => {
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [isWaiting, setIsWaiting] = useState(false);
  const [isValidEmail, setIsValidEmail] = useState(true);




const navigate = useNavigate();
const handleEmailChange = (event) => {
  const newEmail = event.target.value;
  setEmail(newEmail);

  // Regular expression for basic email validation
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  
  setIsValidEmail(emailPattern.test(newEmail));
};

  const handleNameChange = (e) => {
    setName(e.target.value);
  };

  const handleSubmit =  (e) => {
    e.preventDefault();
    if (name.trim() === '' || email.trim() === '') {
      alert('Please fill in all fields.'); // You can show an error message
      return;
    }
    setIsWaiting(true);
    const emailPattern = /@/;

    // if (!emailPattern.test(email)) {
    //   alert('Invalid email format');
    //   return; // Do not proceed further
    // }
    console.log('Email:', email);
    console.log('Name:', name);

    try {
      // Here you can use the axios.post method to send data to the server.
      // Example: 
      axios.post(`${apiUrl}/verifyOtp`, {
        email: email,
        
      }).then((response) => {
        console.log(response);
        const obj = { email: email, name: name, otp: response.data };
        // After a successful response, you can navigate to the verification page.
        navigate('/verifyOTP', {state: { obj: obj }});
      });
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div className="container mt-4" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '70vh' }}>
      <h2></h2>
      <form className="form">
        <p className="title">Register</p>
        <p className="message">Signup now and get full access to our app.</p>
        <div className="flex">
          <label>
            <input
              required
              placeholder=""
              type="text"
              className="input"
              value={name}
              onChange={handleNameChange}
            />
            <span>Name</span>
          </label>
          <label>
            <input
              required
              placeholder=""
              type="email"
              className={`input ${isValidEmail ? '' : 'invalid'}`}
              //className="input"
              value={email}
              onChange={handleEmailChange}
            />
            <span>Email</span>
      {!isValidEmail && <p className="error-message">Please enter a valid email address.</p>}
          </label>
        </div>
        <button className="submit" onClick={handleSubmit}>Submit</button>
        <span
        id="waittorenderotpverification"
        style={{ display: isWaiting ? 'inline' : 'none' }}
      >
        Please wait...
      </span>
        <p className="signin">
          Already have an account? <a href="/login">Sign in</a>
        </p>
      </form>
    </div>
  );
};

export default SignupForm;