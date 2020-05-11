import React, { Component } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import DataTable from "./dataTable";
import ListGroup from "./common/listGroup";
import Pagination from "./common/pagination";
import { getWords, deleteWord } from "../services/dataService";
import { getGenres } from "../services/genreService";
import { paginate } from "../utils/paginate";
import _ from "lodash";
import fire from "../config/firebase";

class Data extends Component {
  state = {
    movies: [],
    genres: [],
    currentPage: 1,
    pageSize: 10,
    searchQuery: "",
    selectedGenre: null,
    sortColumn: { path: "title", order: "asc" },
  };

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

  handleDelete = async (movie) => {
    const originalMovies = this.state.movies;
    const movies = originalMovies.filter((m) => m.id !== movie.id);
    this.setState({ movies });

    try {
      await deleteWord(movie.id);
    } catch (ex) {
      if (ex.response && ex.response.status === 404) console.log("x");
      toast.error("This movie has already been deleted.");
      this.setState({ movies: originalMovies });
    }
  };

  handlePageChange = (page) => {
    this.setState({ currentPage: page });
  };

  handleGenreSelect = (genre) => {
    this.setState({ selectedGenre: genre, searchQuery: "", currentPage: 1 });
  };

  handleSearch = (query) => {
    this.setState({ searchQuery: query, selectedGenre: null, currentPage: 1 });
  };

  handleSort = (sortColumn) => {
    this.setState({ sortColumn });
  };

  getPagedData = () => {
    const {
      pageSize,
      currentPage,
      sortColumn,
      selectedGenre,
      searchQuery,
      movies: allMovies,
    } = this.state;

    let filtered = allMovies;
    if (searchQuery)
      filtered = allMovies.filter((m) =>
        m.title.toLowerCase().startsWith(searchQuery.toLowerCase())
      );
    else if (selectedGenre && selectedGenre._id)
      filtered = allMovies.filter((m) => m.genre._id === selectedGenre._id);

    const sorted = _.orderBy(filtered, [sortColumn.path], [sortColumn.order]);

    const movies = paginate(sorted, currentPage, pageSize);

    return { totalCount: filtered.length, data: movies };
  };

  render() {

    const { pageSize, currentPage, sortColumn, searchQuery } = this.state;
    const { user } = this.props;

    const { totalCount, data: movies } = this.getPagedData();
    return (
      <div className="row">
        <div className="col-3">
          <ListGroup
            items={this.state.genres}
            selectedItem={this.state.selectedGenre}
            onItemSelect={this.handleGenreSelect}
          />
        </div>
        <div className="col">
          <Link
            to="/dataForm"
            className="btn btn-primary"
            style={{ marginBottom: 20 }}
          >
            Add data
          </Link>
          <DataTable
            movies={this.state.movies}
            sortColumn={sortColumn}
            onLike={this.handleLike}
            onDelete={this.handleDelete}
            onSort={this.handleSort}
          />
          <Pagination
            itemsCount={totalCount}
            pageSize={pageSize}
            currentPage={currentPage}
            onPageChange={this.handlePageChange}
          />
        </div>
      </div>
    );
  }
}

export default Data;
