import axios from "axios";
import { BASE_apiUrl } from "../common/constant";
import { AuthHeader } from "./auth.header";

const apiUrl = "http://localhost:9000"

class userService {
    register(user) {
        return axios.post(apiUrl + "/register", user);
    }

    registerEmp(user) {
        return axios.post(apiUrl + "/registerEmp", user);
    }

    login(user) {
        return axios.post(apiUrl + "/login", user);
    }

    checkEmailAndMob(email, mob) {
        return axios.get(apiUrl + "/forgotPassword/" + email + "/" + mob);
    }
    resetPassword(user) {
        return axios.post(apiUrl + "/updatePassword/", user);
    }
    getAllEmp() {
        return axios.get(apiUrl + "/getAllEmp");
    }

    createNetBanking(netbanking) {
        return axios.post(apiUrl + "/createNetbanking", netbanking);
    }

    getAllTrans() {
        return axios.get(apiUrl + "/getAllTrans", { headers: AuthHeader() })
    }

    sendMoney(s)
    {
        return axios.post(apiUrl + "/sendMoney", s,{ headers: AuthHeader() });
    }

    changePassword(psw)
    {
        return axios.post(apiUrl + "/changePassword", psw,{ headers: AuthHeader() });
    }

}

export default new userService();