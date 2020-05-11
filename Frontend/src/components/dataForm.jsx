import React from "react";
import Joi from "joi-browser";
import Form from "./common/form";
import { getWords, saveWord } from "../services/dataService";
import { getGenres } from "../services/genreService";
import fire from "../config/firebase";

class DataForm extends Form {
  state = {
    data: {
      value: "",
    },
    genres: [],
    errors: {},
  };

  schema = {
    id: Joi.number(),
    value: Joi.string().required().label("Value"),
  };

  async populateData() {
    try {
      let dataId = this.props.location.pathname;

      if (dataId === "/dataform") return;
      dataId=dataId.slice(13)

      fire.database().ref().child(dataId).once('value',snap =>{
        const dataResponse= snap.val()
        this.setState({ data: this.mapToViewModel(dataId,dataResponse) });
     })
    } catch (ex) {
      if (ex.response && ex.response.status === 404)
        this.props.history.replace("/not-found");
    }
    console.log(this.state.data);
    
  }

  async componentDidMount() {
    await this.populateData();
  }

  mapToViewModel(dataId,dataModel) {
    console.log(dataModel);

    if (!dataModel.value){
      const data ={id:null,value:""} 
      data.value= dataModel;
      data.id=Number(dataId)
      console.log(data);
      return data;
    }else {
      dataModel=dataModel.value
      const data ={id:null,value:""} 
      data.value= dataModel;
      data.id=Number(dataId)
      console.log(data);
      return data;
    }
  }

  doSubmit = async () => {
    await saveWord(this.state.data);
    console.log(this.state.data);
    setTimeout(() => {  window.location = "/" }, 2000);
    
  };

  render() {
    return (
      <div>
        <h1>Value</h1>
        <form onSubmit={this.handleSubmit}>
          {this.renderInput("value", "")}         
          {this.renderButton("Save")}
        </form>
      </div>
    );
  }
}

export default DataForm;
