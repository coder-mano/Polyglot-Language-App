import http from "./httpService";
import { apiUrl } from "../config.json";
import jwtDecode from "jwt-decode";
import fire from "../config/firebase";

//const apiEndpoint = `${apiUrl}/auth/login`;
const tokenKey = "token";

http.setJwt(getJwt());

export async function login(userName, password) {
   fire.auth().signInWithEmailAndPassword(userName, password).then((data)=>{
     console.log(data.user);
      const jwt=data.user["xa"]
      return localStorage.setItem(tokenKey, jwt);
  }).catch((error)=>{
    console.log(error);
  })  
}

export function loginWithJwt(jwt) {
  console.log(jwt);
  localStorage.setItem(tokenKey, jwt);
}

export function logout() {
  fire.auth().signOut();
  localStorage.removeItem(tokenKey);
}

export function getCurrentUser() {
  try {
    const jwt = localStorage.getItem(tokenKey);
    //console.log(jwt);
    return jwtDecode(jwt);
  } catch (ex) {
    return null;
  }
}

export function getJwt() {
  return localStorage.getItem(tokenKey);
}

export default {
  login,
  loginWithJwt,
  logout,
  getCurrentUser,
  getJwt,
};
