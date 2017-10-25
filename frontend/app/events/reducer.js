import { combineReducers } from 'redux'
import { isOldEvent, isNewEvent, sortByTimestampAsc, sortByTimestampDesc }  from '../utils/Utils';


import {
	REQUEST_EVENTS,
	RECEIVE_EVENTS,
    REFRESH_EVENTS,
	REQUEST_OLD_EVENTS,
	RECEIVE_OLD_EVENTS,
    REFRESH_OLD_EVENTS,
	TOGGLE_OLD_EVENTS
} from './actions'

const initialState = {
	upcomingEvents: [],
	oldEvents: [],
	loading: false,
	successful: false,
	messages: [],
	errors: [],
};

const initialToggleState = {
	visibleHistory: false,
};

function toggleOldEvents(state = initialToggleState, action) {
	switch(action.type) {
		case TOGGLE_OLD_EVENTS:
			return {...state, visibleHistory: !state.visibleHistory};
		default: 
			return state;
	}
}

function fetchEventList(state = initialState, action) {
	switch (action.type) {
        case REFRESH_EVENTS:
		case REQUEST_EVENTS:
			return {...state, loading: true, items: [] };

		case RECEIVE_EVENTS:
			return {...state, loading: false, items: action.events, lastUpdated: action.receivedAt };
		default:
			return state;
	}
}

function fetchOldEventList(state = initialState, action) {
	switch (action.type) {
        case REFRESH_OLD_EVENTS:
		case REQUEST_OLD_EVENTS:
			return {...state, loading: true, items: [] };
		case RECEIVE_OLD_EVENTS:
			return {...state, loading: false, items: action.events, lastUpdated: action.receivedAt };
		default:
			return state;
	}
}

function mapOld(events) {
	return events.filter(isOldEvent).sort(sortByTimestampDesc)

}
function mapUpComing(events) {
	return events.filter(isNewEvent).sort(sortByTimestampAsc)
}

function events(state = { }, action) {
  switch (action.type) {
    case REQUEST_EVENTS:
    case RECEIVE_EVENTS:
    case REFRESH_EVENTS:
    	var newState = fetchEventList(state["events"], action);
      	return { ...state, ...newState, oldEvents: mapOld(newState.items), upcomingEvents: mapUpComing(newState.items) }
    case REQUEST_OLD_EVENTS:
    case RECEIVE_OLD_EVENTS:
    case REFRESH_OLD_EVENTS:
    	var newState = fetchOldEventList(state["events"], action);
      	return { ...state, ...newState, oldEvents: mapOld(newState.items), upcomingEvents: mapUpComing(newState.items) }
    default:
      	return state
  }
}

const rootReducer = combineReducers({
  allEvents: events,
  toggleOldEvents
});

export default rootReducer
