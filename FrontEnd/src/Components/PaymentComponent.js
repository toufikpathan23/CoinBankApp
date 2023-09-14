import React, { useState } from "react";

import { useLocation ,useNavigate} from "react-router-dom";
import AccountsService from "../services/accounts.service";
import './PaymentComponent.css'

const PaymentComponent = () => {
  const [paymentId, setPaymentId] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const AccountService = new AccountsService();
  const amt = location.state.amount;
  const openRazorpay = async () => {
    //await loadRazorpay(); // Load the Razorpay script

    // const { data } = await fetch('/create-order', { method: 'POST' }); // Server route to create order on your backend
    //const { id } = data;
    //key_id,key_secret
    //,7slJQaI3HIVIyumtqHe0FPah
   
    const options = {
      key: "rzp_test_AglkU8qdyIBCle",
      amount: amt * 100, // Amount in paisa (e.g., for Rs. 100, specify 10000)
      currency: "INR",
      // order_id: 'TEST123456789',
      name: "CoinBank",
      description: "Payment for services",
      handler: function (response) {
        setPaymentId(response.razorpay_payment_id);
        AccountService.makeCredit(location.state.id, amt, location.state.description);
        navigate(`/one-account/${location.state.id}`)
        // Handle success/failure and other operations here
      },
    };

    const rzp = new window.Razorpay(options);
    rzp.open();
  };

  return (
    <div  style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100vh' }}><h2>Your account will be credited with {amt} Rs on successful transaction.</h2>
      <button onClick={openRazorpay}  className="proceed-button">Proceed to Pay </button>
      {paymentId && <p>Payment successful! Payment ID: {paymentId}</p>}
    </div>
  );
};

export default PaymentComponent;
