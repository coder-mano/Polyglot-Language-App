import http from "./httpService";
import { apiUrl } from "../config.json";
import { getJwt } from "./authService";
import fire from "../config/firebase";
import React, { Component } from "react";
import "firebase/firestore";
import * as firebase from "firebase/app"
import ReactObserver from 'react-event-observer';
import { getFirebase } from 'react-redux-firebase'
import { combineEpics } from 'redux-observable'
import StoreConnect from "../config/redux/storeConnect"

  
export async function getWords () {
    fire.database().ref().child("db_size").once('value',snap =>{
    const count=snap.val()+1 
    console.log(snap);
    console.log(count);
   return fire.database().ref().child(count).once()
 })
}
   
export async function saveWord(word) {
  console.log(word);
  

  if (word.id) {
    const body = { ...word };
    console.log(body);
    delete body.id;
    console.log(body);

    return fire.database().ref().child(word.id.toString()).set(body) 
  } 
  fire.database().ref().child("db_size").once('value',snap =>{
    const count=Number(snap.val())+1 
    word = word.value;
   fire.database().ref().child("db_size").set(count)
   return fire.database().ref().child(count.toString()).set(word)
 })
 
}

export function deleteWord(movieId) {
    fire.database().ref().child("db_size").set(movieId-1)
    return fire.database().ref().child(movieId).remove()
 }

