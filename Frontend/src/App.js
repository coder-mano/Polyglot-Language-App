import React, { Component } from "react";
import { Route, Redirect, Switch } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import NavBar from "./components/navBar";
import LoginForm from "./components/loginForm";
import Logout from "./components/logout";
import auth from "./services/authService";
import ProtectedRoute from "./components/common/protectedRoute";
import DataTable from "./components/dataTable";
import DataForm from "./components/dataForm";
import Data from "./components/data";
import "react-toastify/dist/ReactToastify.css";
import "./App.css";
import axios from "axios";
import fire from "./config/firebase"


 
class App extends Component {
  
   state = {
    //user:{}
   };

   
  // constructor(props){
  //   //super(props)
  //   this.state={
  //     user:{}
  //   }
  // }

  componentDidMount()
  {
    // const user = auth.getCurrentUser();
    // this.setState({ user });
    // console.log(user);
    //this.authListener()

  }

  authListener(){
    fire.auth().onAuthStateChanged((user)=>{
      if(user)
      {
        const user = auth.getCurrentUser();
        this.setState({ user });
        console.log(user);
      }
      else{
        const user = auth.getCurrentUser();
        this.setState({user : null})
        console.log(user);
      }
    })
  }

  render() {

    const { user } = this.state;
    //console.log(this.state);

    return (
      <React.Fragment>
        <ToastContainer />
        <NavBar user={user} />
        <main className="container">
          <Switch>
            <Route path="/login" component={LoginForm} />
            <Route path="/logout" component={Logout} />
            {/*<Route path="/dataTable" component={DataTable} />*/}
            <Route path="/dataForm" component={DataForm} />
            <ProtectedRoute path="/data/:id" component={DataForm} />
            <ProtectedRoute path="/data" component={Data} />
            {!user &&  (<Redirect from="/" exact to="/login" />)}
          
            {user &&  (<Route from="/" exact to="/dog" />)}
            {/* <Route
              path="/dogs"
              render={(props) => <Dogs {...props} user={this.state.user} />} //schovanie componentov
            /> */}
            {/*<Route path="/not-found" component={NotFound} />
            <Redirect to="/not-found" />*/}
          </Switch>
        </main>
      </React.Fragment>
    );
  }
}

export default App;
