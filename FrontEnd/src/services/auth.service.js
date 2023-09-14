import { useState } from 'react';
import axios from 'axios';
import { apiUrl } from '../common/constant';

const clearLocalStorage = () => {
  localStorage.clear();
};

 const AuthService = () => {
  const [username, setUsername] = useState('');
  
  const login = async (user) => {
    try {
      const response = await axios.post(`${apiUrl}/login`, user);
      const tokens = response.data;
      doLoginUser(user.username, tokens);
      return true;
    } catch (error) {
      // Handle error and display a message to the user
      console.error(error);
      return false;
    }
  };

  const WohLogin = () => {
    return username;
  };

  const logout = () => {
    clearLocalStorage();
    doLogoutUser();
  };

  const isLoggedIn = () => {
    return !!getJwtToken();
  };

  const refreshToken = async () => {
    try {
      const response = await axios.post(`${apiUrl}/refresh`, {
        refreshToken: getRefreshToken(),
      });
      const tokens = response.data;
      storeJwtToken(tokens.jwt, tokens.roles);
    } catch (error) {
      console.error(error);
    }
  };

  const getJwtToken = () => {
    return localStorage.getItem('JWT_TOKEN');
  };

  const doLoginUser = (username, tokens) => {
    setUsername(username);
    storeTokens(tokens);
  };

  const doLogoutUser = () => {
    setUsername('');
    removeTokens();
  };

  const getRefreshToken = () => {
    return localStorage.getItem('REFRESH_TOKEN');
  };

  const storeJwtToken = (jwt, roles) => {
    localStorage.setItem('JWT_TOKEN', jwt);
    localStorage.setItem('ROLES', roles);
    localStorage.setItem('username', username);
  };

  const storeTokens = (tokens) => {
    localStorage.setItem('JWT_TOKEN', tokens.jwt);
    localStorage.setItem('REFRESH_TOKEN', tokens.refreshToken);
    localStorage.setItem('ROLES', tokens.roles);
    localStorage.setItem('username', username);
  };

  const removeTokens = () => {
    localStorage.removeItem('JWT_TOKEN');
    localStorage.removeItem('REFRESH_TOKEN');
    localStorage.removeItem('ROLES');
    localStorage.removeItem('username');
  };

  return {
    login,
    WohLogin,
    logout,
    isLoggedIn,
    refreshToken,
  };
};

export default AuthService;