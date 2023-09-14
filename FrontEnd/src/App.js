import logo from "./logo.svg";
import "./App.css";

import { BrowserRouter, Route, Routes } from "react-router-dom";
import LoginPageComponent from "./Components/LoginPageComponent";
import SignupForm from "./Components/SignUpForm";
import OTPVerificationForm from "./Components/OTPVerificationForm";
import OneAccountComponent from "./Components/OneAccountComponent";
import CustomerAccountsComponent from "./Components/CustomerAccountComponent";
import Navbar from "./component/Navbar";

import "react-toastify/dist/ReactToastify.css";
import { MainPage } from "./component/Admin-Navbar/MainPage";

// import UHome from "./pages/user/UHome";

import { AuthGuard } from "./guard/auth.guard";
import PaymentComponent from "./Components/PaymentComponent";
import ManageCustomersComponent from "./Components/ManagerCustomersComponent";
import UpdateCustomerComponent from "./Components/UpdateCustomerComponent";
import ChangePasswordComponent from "./Components/ChangePasswordComponent";
import { Footer } from "./component/Footer";
import ContactUsComponent from './Components/contactUsComponent';
import WithDrawRequests from './Components/WithDrawRequests';
import LoanComponent from "./Components/LoanComponent";
import FDComponent from "./Components/FDComponent";

function App() {

  return (
    <div>
      <body>
      <BrowserRouter>
        <Navbar></Navbar>
        
        <Routes>
          {/* <Route path='/login' element={<Login />}></Route>
        <Route path='/register' element={<Signup />}></Route>
        <Route path='/401' element={<UnAuthorized />}></Route>
        <Route path='/forgotPassword' element={<ForgotPassword />}></Route> */}
          <Route
            path="/"
            element={<LoginPageComponent ></LoginPageComponent>}
          ></Route>
          <Route
            path="/login"
            element={<LoginPageComponent></LoginPageComponent>}
          ></Route>
          <Route
            path="/customers"
            element={<ManageCustomersComponent></ManageCustomersComponent>}
          ></Route>
          <Route
            path="/payment"
            element={<PaymentComponent></PaymentComponent>}
          ></Route>
           <Route
            path="/withdrawreqs"
            element={<WithDrawRequests></WithDrawRequests>}
          ></Route>

          <Route
            path="/verifyOtp"
            element={<OTPVerificationForm></OTPVerificationForm>}
          ></Route>
          <Route path="/signup" element={<SignupForm></SignupForm>}></Route>
          <Route path="/register" element={<SignupForm></SignupForm>}></Route>
          <Route
            path="/one-account/:id"
            element={<OneAccountComponent></OneAccountComponent>}
          />

          <Route
            path="/customer-accounts/:id"
            element={<CustomerAccountsComponent></CustomerAccountsComponent>}
          />
           <Route
            path="update-customer/:id"
            element={<UpdateCustomerComponent></UpdateCustomerComponent>}
          />
          <Route
            path="loansection/:id"
            element={<LoanComponent></LoanComponent>}
          />
          <Route
            path="fdsection/:id"
            element={<FDComponent></FDComponent>}
          />
           <Route
            path="/forgotPass"
            element={<ChangePasswordComponent></ChangePasswordComponent>}
          />
          <Route
            path="/contactUs"
            element={<ContactUsComponent></ContactUsComponent>}
          />
          <Route path="/user/*">
            //{" "}
            {/* <Route
              path="uhome"
              element={
                <AuthGuard roles={["USER"]}>
                  <UHome />
                </AuthGuard>
              }
            ></Route> */}
            {/* //           <Route path='allTransaction' element={<AuthGuard roles={['ROLE_USER']}><AllTransaction /></AuthGuard>}></Route>
//           <Route path='balance' element={<AuthGuard roles={['ROLE_USER']}><Balance /></AuthGuard>}></Route>
//           <Route path='changePassword' element={<AuthGuard roles={['ROLE_USER']}><ChangePassword /></AuthGuard>}></Route>
//           <Route path='sendMoney' element={<AuthGuard roles={['ROLE_USER']}><SendMoney /></AuthGuard>}></Route>
//           <Route path='viewProfile' element={<AuthGuard roles={['ROLE_USER']}><UViewProfile /></AuthGuard>}></Route> */}
            //{" "}
          </Route>
        </Routes>
        {/* <Footer></Footer> */}
      </BrowserRouter></body>
    </div>
  );
}

export default App;

// <Navbar />
//       <Routes>
//         <Route path='/' element={<Index />}></Route>
//         <Route path='/login' element={<Login />}></Route>
//         <Route path='/register' element={<Signup />}></Route>
//         <Route path='/401' element={<UnAuthorized />}></Route>
//         <Route path='/forgotPassword' element={<ForgotPassword />}></Route>
//         <Route path='/netbanking' element={<NetBanking />}></Route>
//         {/* <Route path='/user/applyJob/:id' element={<ApplyJob />}></Route> */}

//         {/* <Route path='/user/*'>
//         <Route path='applyJob/:id' element={<AuthGuard roles={['ROLE_USER']}><ApplyJob /></AuthGuard>} />
//         <Route path='appliedJob' element={<AuthGuard roles={['ROLE_USER']}><AppliedJob /></AuthGuard>} />
//       </Route> */}

//         <Route path='/admin/*' element={
//           <AuthGuard roles={['ROLE_ADMIN']}>
//             <MainPage />
//           </AuthGuard>
//         }> </Route>

//         <Route path='/emp/*' element={
//           <AuthGuard roles={['ROLE_EMP']}>
//             <MainPage />
//           </AuthGuard>
//         }></Route>

//         <Route path='/user/*' >
//           <Route path='uhome' element={<AuthGuard roles={['ROLE_USER']}><UHome /></AuthGuard>}></Route>
//           <Route path='allTransaction' element={<AuthGuard roles={['ROLE_USER']}><AllTransaction /></AuthGuard>}></Route>
//           <Route path='balance' element={<AuthGuard roles={['ROLE_USER']}><Balance /></AuthGuard>}></Route>
//           <Route path='changePassword' element={<AuthGuard roles={['ROLE_USER']}><ChangePassword /></AuthGuard>}></Route>
//           <Route path='sendMoney' element={<AuthGuard roles={['ROLE_USER']}><SendMoney /></AuthGuard>}></Route>
//           <Route path='viewProfile' element={<AuthGuard roles={['ROLE_USER']}><UViewProfile /></AuthGuard>}></Route>
//         </Route>

//       </Routes>
