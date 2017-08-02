import { combineReducers } from 'redux'
import { isOldEvent, isNewEvent }  from '.././Utils';


import {
	REQUEST_EVENT,
	RECEIVE_EVENT,
    SELECT_EVENT,
    REQUEST_REGISTER_FOR_EVENT,
    RECEIVE_REGISTER_FOR_EVENT
} from './actions'

const initialState = {
	currentEvent: {
        participants: []
    },
    error: "",
    loading: true
}

function mapState(state = initialState, action) {
	switch (action.type) {
		case REQUEST_EVENT:
        case SELECT_EVENT:
            return {...state, loading: true};
        case RECEIVE_EVENT:
			return {...state, loading: false, currentEvent: action.event, lastUpdated: action.receivedAt };
		default:
			return state;
	}
}

function event(state = initialState, action) {
  switch (action.type) {
    case REQUEST_EVENT:
    case SELECT_EVENT:
    case RECEIVE_EVENT:
        var newState = mapState(state.event, action);
        return { ...state, ...newState}
    default:
        return state
  }
}

function registerForEvent(state = {}, action) {
  switch (action.type) {
    case RECEIVE_REGISTER_FOR_EVENT:
        debugger;
        if (action.event.error) {
            return {error: action.event.error.message};
        }
        // TODO: What action comes here?
        return {};
    default:
        return {}
  }
}

const rootReducer = combineReducers({
	event,
    register: registerForEvent
})

export default rootReducer