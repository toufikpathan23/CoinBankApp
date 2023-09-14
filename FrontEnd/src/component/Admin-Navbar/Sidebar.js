import React, { useEffect } from 'react';
import './assets/css/style.css';
import './assets/vendor/bootstrap/css/bootstrap.min.css';
import './assets/vendor/bootstrap-icons/bootstrap-icons.css';
import './assets/img/apple-touch-icon.png';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { clearCurrentUser } from '../../store/action/user.action';

const Sidebar = () => {

    const loginUser = useSelector(state => state.user);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const logout = () => {
        const st = window.confirm("Do you want logout");
        if (st) {
            dispatch(clearCurrentUser());
            navigate("/login")
        }
    }



    return (
        <>


            {
                loginUser.role === 'ROLE_ADMIN' &&

                <aside id="sidebar" className="sidebar" >

                    <ul className="sidebar-nav" id="sidebar-nav">

                        <li className="nav-item">
                            <Link to="/admin/ahome" className="nav-link ">
                                <i className="bi bi-grid"></i>
                                <span>Dashboard</span>
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link to="status" className="nav-link collapsed">
                                <i class="fas fa-id-card-alt"></i>
                                <span>Status</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="account" className="nav-link collapsed">
                                <i class="fa-solid fa-file-invoice"></i>
                                <span>Account</span>
                            </Link></li>


                        <li className="nav-item">
                            <Link to="transaction" className="nav-link collapsed" >
                                <i
                                    class="fa-solid fa-bookmark"></i>
                                <span>Trasaction</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="viewTransaction" className="nav-link collapsed">
                                <i class="fa-solid fa-list"></i>
                                <span>View Transaction</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="addEmp" className="nav-link collapsed">
                                <i
                                    class="fas fa-user-circle"></i>
                                <span>Add Emp</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="ViewEmp" className="nav-link collapsed">
                                <i class="fa-solid fa-list"></i>
                                <span>Emp Details</span>
                            </Link></li>

                        <li className="nav-item">
                            <a className="nav-link collapsed" onClick={() => logout()}>
                                <i class="fa-solid fa-right-from-bracket"></i>
                                <span>Logout</span>
                            </a></li>
                    </ul>
                </aside>

            }


            {
                loginUser.role === 'ROLE_EMP' &&

                <aside id="sidebar" className="sidebar" >

                    <ul className="sidebar-nav" id="sidebar-nav">

                        <li className="nav-item">
                            <Link to="/emp/ehome" className="nav-link ">
                                <i className="bi bi-grid"></i>
                                <span>Dashboard</span>
                            </Link>
                        </li>



                        <li className="nav-item">
                            <Link to="account" className="nav-link collapsed">
                                <i class="fa-solid fa-file-invoice"></i>
                                <span>Account</span>
                            </Link></li>


                        <li className="nav-item">
                            <Link to="transaction" className="nav-link collapsed" >
                                <i class="fa-solid fa-bookmark"></i>
                                <span>Trasaction</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="viewTransaction" className="nav-link collapsed">
                                <i class="fa-solid fa-list"></i>
                                <span>View Transaction</span>
                            </Link></li>

                        <li className="nav-item">
                            <Link to="addCustomer" className="nav-link collapsed">
                                <i class="fas fa-user-circle"></i>
                                <span>Add Customer</span>
                            </Link></li>

                        <li className="nav-item">
                            <a className="nav-link collapsed" onClick={() => logout()}>
                                <i class="fa-solid fa-right-from-bracket"></i>
                                <span>Logout</span>
                            </a></li>
                    </ul>
                </aside>

            }



        </>
    )
}

export { Sidebar };
