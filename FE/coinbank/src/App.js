import logo from "./logo.svg";
import "./App.css";

import { BrowserRouter, Route, Routes } from "react-router-dom";
import LoginPageComponent from "./Components/LoginPageComponent";
import SignupForm from "./Components/SignUpForm";
import OTPVerificationForm from "./Components/OTPVerificationForm";
import OneAccountComponent from "./Components/OneAccountComponent";
import CustomerAccountsComponent from "./Components/CustomerAccountComponent";

function App() {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route
            path="/"
            element={<LoginPageComponent></LoginPageComponent>}
          ></Route>
          <Route
            path="/login"
            element={<LoginPageComponent></LoginPageComponent>}
          ></Route>

          <Route
            path="/verifyOtp"
            element={<OTPVerificationForm></OTPVerificationForm>}
          ></Route>
          <Route path="/signup" element={<SignupForm></SignupForm>}></Route>

          <Route
            path="/one-account/:id"
            element={<OneAccountComponent></OneAccountComponent>}
          />

          <Route
            path="/customer-accounts/:id"
            element={<CustomerAccountsComponent></CustomerAccountsComponent>}
          />

          {/* <Route path="/customers" component={ManageCustomersComponent} />
        <Route path="/accounts" component={ManageAccountsComponent} />
        <Route path="/Account/:id" component={ManageAccountsComponent} />
        <Route path="/add-customer" component={AddCustomerComponent} />
        <Route
          path="/update-customer/:id"
          component={UpdateCustomerComponent}
        />
         */}
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
