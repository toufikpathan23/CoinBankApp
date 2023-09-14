import store from "../store/store"

export const AuthHeader = () => {
    const loginUser = store.getState().user;
    const token = loginUser.token;

    return {
        'Content-Type': 'application/json',
        'authorization': 'Bearer ' + token 
    }

}