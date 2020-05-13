<p align="center">
  <a href="" rel="noopener">
 <img width=584px height=400px src="https://dwglogo.com/wp-content/uploads/2017/09/1460px-React_logo.png" alt="React logo"></a>
</p>

<h3 align="center">React Web application</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
🔗[Link to the database](https://zct.netlify.app/login)🔗

</div>

---

## 📝 Table of Contents

- [About](#about) 
- [Demo / Working](#demo)
- [How it works](#working)
- [Getting Started](#getting_started)
- [Deployment](#deployment)
- [Built Using](#built_using)
- [Access to our database](#acces)
- [Author](#author)


## 🧐 About <a name = "about"></a>

This repository represents the administrator's access to the Real-Time Firebase database & contains a React web application for performing basic CRUD operations like Create, Read, Update, and Delete with Login and Logout.


## 🎥 Demo / Working <a name = "demo"></a>

![Working](https://media.giphy.com/media/sRFEa8lbeC7zbcIZZR/giphy.gif)

---
## 💭 How it works <a name = "working"></a>
💡We connect to the firebase database using the login function. After logging in, we can create, edit or delete individual values ​​in the database. These functions the main part of our web application.


#### ✔️ Create ✔️

##### Input: inserted word

```js
export async function saveWord(word) {
 
  if (word.id) {
    const body = { ...word };
    delete body.id;
    return fire.database().ref().child(word.id.toString()).set(body) 
  } 
  fire.database().ref().child("db_size").once('value',snap =>{
    const count=Number(snap.val())+1 
    word = word.value;
   fire.database().ref().child("db_size").set(count)
   return fire.database().ref().child(count.toString()).set(word)
 })
}
```
##### Output: saved word

---
#### ✔️Read ✔️
```js
  async componentDidMount() {
    let db_size;
    let i;
    let movies1=[];
    let movies=[];

   await fire.database().ref().child("db_size").once('value',snap =>{
       db_size=snap.val();
       return this.setState({db_size});
    })
    
    for (i = 1; i <= db_size; i++) {
      await fire.database().ref().child(i).once('value',snap =>{
        movies1.push(snap.val())
     })
    }

    let keys = Object.keys(movies1)
    let data = Object.values(movies1)

    for(let i=0; i<keys.length;i++){
      let oldid = keys[i]
      let score = Object.values(data[oldid])
      let id = (parseInt(oldid)+1).toString()
     let value1=(score)
      movies= [...movies,{id,value1}]
    }
    this.setState({movies})
  }
```
#####    Output: all words in our database

---

#### ✔️Update ✔️
##### Input: word


    
```js
export async function saveWord(word) {
 
  if (word.id) {
    const body = { ...word };
    delete body.id;
    return fire.database().ref().child(word.id.toString()).set(body) 
  } 
  fire.database().ref().child("db_size").once('value',snap =>{
    const count=Number(snap.val())+1 
    word = word.value;
   fire.database().ref().child("db_size").set(count)
   return fire.database().ref().child(count.toString()).set(word)
 })
}
```
##### Output: updated word

---

#### ✔️ Delete ✔️

##### Input: wordId
    
    
```js
export function deleteWord(wordId) {
    fire.database().ref().child("db_size").set(wordId-1)
    return fire.database().ref().child(wordId).remove()
}
```


##### Output: deleted word



---
#### ✔️Login ✔️

#####   Input: userName, password

```js
export async function login(userName, password) {
   fire.auth().signInWithEmailAndPassword(userName, password).then((data)=>{
      const jwt=data.user["xa"]
      return localStorage.setItem(tokenKey, jwt);
  }).catch((error)=>{
    console.log(error);
  })  
}
```
#####   Output: tokenKey, jwt  



---
#### ✔️Logout ✔️
```js
export function logout() {
  fire.auth().signOut();
  localStorage.removeItem(tokenKey);
}
```

---


## 🏁 Getting Started <a name = "getting_started"></a>

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See [deployment](#deployment) for notes on how to deploy the project on a live system.

### 👷 Running Locally

```
$ git clone https://github.com/Coder-mano/Assignment.git
$ cd Frontend/src
```

### 👷 Installing
The generated project includes React and ReactDOM as dependencies. It also includes a set of scripts used by Create React App as a development dependency. You may install other dependencies (for example, React Router) with npm:
```
npm install --save react-router
```
Alternatively you may use yarn:

```
yarn add react-router
```
This works for any library, not just react-router.



### 👷 Available Scripts
In the project directory, you can run:
```
npm start
```
Runs the app in the development mode. Open http://localhost:3000 to view it in the browser.

The page will reload if you make edits. You will also see any lint errors in the console.
```
npm test
```
Launches the test runner in the interactive watch mode. See the section about running tests for more information.
```
npm run build
```
Builds the app for production to the build folder. It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes. Your app is ready to be deployed!

See the section about deployment for more information.
```
npm run eject
```
Note: this is a one-way operation. Once you eject, you can’t go back!


---

## 🚀 Deployment <a name = "deployment"></a>

🔗 **Netlify**:  https://zct.netlify.app/

🔗 **GitHub**: https://github.com/Coder-mano/Polygloth-Pronunciation-Training/tree/master/web


## ⛏️ Built Using <a name = "built_using"></a>

🔗 [Firebase](https://console.firebase.google.com/u/0/?hl=en) - Real Time Database
🔗 [Netlify](https://www.netlify.com/) - SaaS hosting platform

## 🔐 Access to our database <a name = "access"></a>🔑

    🔑 email: admin@gmail.com  
    🔑 password: qqqqqq






