import React, { Component } from "react";
import { Link } from "react-router-dom";
import Table from "./common/table";
import authService from "../services/authService";

class DataTable extends Component {
  columns = [
    {
      path: "id",
      label: "Id",
      content: (movie) => (
        <Link to={`/data/detail/${movie.id}`}>{movie.id}</Link>
      ),
    },
    {
      path: "value1",
      label: "Value",
    },
  ];

  deleteColumn = {
    key: "delete",
    content: (movie) => (
      <button
        onClick={() => this.props.onDelete(movie)}
        className="btn btn-danger btn-sm"
      >
        Delete
      </button>
    ),
  };
  constructor() {
    super();
    const user = authService.getCurrentUser();
    //if (user && user.isAdmin) this.columns.push(this.deleteColumn);
    this.columns.push(this.deleteColumn);
  }

  render() {
    const { movies, onSort, sortColumn } = this.props;
    //console.log(movies);
    
    return (
      <Table
        columns={this.columns}
        data={movies}
        sortColumn={sortColumn}
        onSort={onSort}
      />
    );
  }
}

export default DataTable;
