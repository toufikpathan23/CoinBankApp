import React, { useState } from 'react';
import './OTPVerification.css'; // Import the CSS file
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { apiUrl } from '../common/constant';


const OTPVerificationForm = () => {
  const location = useLocation();
  const optData = location.state.obj.otp; // Access the passed state
  const email=location.state.obj.email;
  const name=location.state.obj.name;
  const [otp, setOtp] = useState('');
const navigate = useNavigate();
  const handleOtpChange = (e) => {
    setOtp(e.target.value);
  };

  const handleVerify = async (e) => {
    e.preventDefault();

  
    if (otp === (optData.otp)) {
      try {
       
        const response = await axios.post(`${apiUrl}/customers/save`, {
          email: email,name:name
        });
        alert("you can login now!!! your name is your password")
        navigate('/login')
      
      } catch (error) {
       
      }
    } else {
      // Handle incorrect OTP
      alert('Incorrect OTP');
    }
  };

  return (<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}> <form className="form"  >
      <div className="title">OTP</div>
      <div className="title">Verification Code</div>
      <p className="message">We have sent a verification code to your mobile number</p>
      <div className="inputs">
        <input
          type="text"
          maxLength="4"
          value={otp}
          onChange={handleOtpChange}
        />
        {/* ... repeat for other input fields ... */}
      </div>
      <button className="action" onClick={handleVerify}>Verify Me</button>
      <button className="action1" onClick={()=>{navigate('/register')}}>Did'nt Recieve OTP</button>
    </form></div>
   
  );
};

export default OTPVerificationForm;