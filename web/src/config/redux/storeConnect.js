import React from 'react'
import { compose } from 'redux' // can also come from recompose
import { withHandlers } from 'recompose'
import { withFirestore } from 'react-redux-firebase'

function AddTodo({ addTodo }) {
  return (
    <div>
      <button onClick={addTodo}>
        Add Sample Todo
      </button>
    </div>
  )
}

const enhance = compose(
  withFirestore,
  withHandlers({
    addTodo: props => () => {
      const newTodo = { done: false, text: 'Sample' }
      return props.firestore.add({ collection: 'todos' }, newTodo)
    }
  })
)

export default enhance(AddTodo)