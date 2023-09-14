import { createStore, combineReducers } from 'redux';
import userReducer from './reducer/user.reducer';

const rootReducer = combineReducers({
  user: userReducer,
  // other reducers
});

const store = createStore(rootReducer);

export default store;
