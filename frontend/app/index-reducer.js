import { combineReducers } from 'redux'
import events from './events/reducer'
import event from './event/reducer'

 const IndexReducer = combineReducers({
  events,
  event
})

export default IndexReducer