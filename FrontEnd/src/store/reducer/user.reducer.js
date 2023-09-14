// reducers/userReducer.js
import { SET_CURRENT_USER, CLEAR_CURRENT_USER } from '../type';

const initialState = null; // or {}

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_CURRENT_USER:
      return action.payload;
    case CLEAR_CURRENT_USER:
      return null;
    default:
      return state;
  }
};

export default userReducer;
