import React from 'react'
import { useSelector } from 'react-redux';

import { AdminNavbar } from './admin-navbar'
import { NavPage } from './NavPage';
import { Sidebar } from './Sidebar';


const MainPage = () => {
    return (
        <div>
            <AdminNavbar />
            <div className="container-fluid p-0 m-0">
                <div className="row">
                    <div className="col-md-3 p-0">
                        <Sidebar />
                    </div>
                    <div className="col-md-9 p-0" style={{marginLeft:-40}}>
                        <NavPage />
                    </div>
                </div>
            </div>
        </div>
    )
}

export { MainPage };
