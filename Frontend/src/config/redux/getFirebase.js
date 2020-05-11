import { applyMiddleware, compose, createStore } from 'redux';
import thunk from 'redux-thunk';
import { reactReduxFirebase } from 'react-redux-firebase';
import makeRootReducer from './reducers';
import { getFirebase } from 'react-redux-firebase';

const fbConfig = {} // your firebase config

const store = createStore(
  makeRootReducer(),
  initialState,
  compose(
    applyMiddleware([
      // Pass getFirebase function as extra argument
      thunk.withExtraArgument(getFirebase)
    ]),
    reactReduxFirebase(fbConfig)
  )
);
// then later
export const addTodo = (newTodo) =>
 (dispatch, getState, getFirebase) => {
   const firebase = getFirebase()
   firebase
     .push('todos', newTodo)
     .then(() => {
       dispatch({ type: 'SOME_ACTION' })
     })
};