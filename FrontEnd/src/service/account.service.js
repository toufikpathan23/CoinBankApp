import axios from "axios";
import { BASE_apiUrl } from "../common/constant";
import { AuthHeader } from "./auth.header";

const apiUrl = "http://13.235.77.5 :5000"

class accountService {

    accountSatusUpdate(id, st) {
        return axios.get(apiUrl + "/statusUpdate/" + id + "?st=" + st);
    }

    getAllPendingAccount() {
        return axios.get(apiUrl + "/getAllPendigAccount");
    }

    getAllApproveAccount() {
        return axios.get(apiUrl + "/getAllApproveAccount");
    }

    getAccountDetails(accno) {
        return axios.get(apiUrl + "/getAccountDetils?accno=" + accno);
    }

    doTransactionByAdmin(trans) {
        return axios.post(apiUrl + "/doTransactionByAdmin", trans);
    }

    getTransaction(accno) {
        return axios.get(apiUrl + "/getTransaction?accno=" + accno);
    }
    getAccountById(id) {
        return axios.get(apiUrl + "/getAccountById/" + id);
    }
    updateAccount(user) {
        return axios.post(apiUrl + "/updateAccount", user);
    }
}

export default new accountService();