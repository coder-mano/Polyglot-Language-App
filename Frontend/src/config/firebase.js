import firebase from 'firebase';

let firebaseConfig = {
    apiKey: "AIzaSyCZdHAP8wzYOHSgQdYkbJkHJLFI_-9bGp4",
    authDomain: "assignment-1d30b.firebaseapp.com",
    databaseURL: "https://assignment-1d30b.firebaseio.com",
    projectId: "assignment-1d30b",
    storageBucket: "assignment-1d30b.appspot.com",
    messagingSenderId: "1075805763134",
    appId: "1:1075805763134:web:1423e3500b6319e5da836f"
  };

const fire = firebase.initializeApp(firebaseConfig);
export default fire;

