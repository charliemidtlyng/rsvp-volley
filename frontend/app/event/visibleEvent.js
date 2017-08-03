import Event from './event'
import { selectEvent, requestRegisterForEvent, requestUnregisterForEvent } from './actions'
import { connect } from 'react-redux';
// maps redux store state to components
const mapStateToEventProps = (state) => {
    debugger;
    return {...state.event};
    //return {...state.events.allEvents, visibleHistory: state.events.toggleOldEvents.visibleHistory, event: state.event.event };
};

// // maps redux store dispatch to list of components
const mapFetchEventToProps = (dispatch) => {
    return {
        fetchEventById: id => {
            dispatch(selectEvent(id));
        },
        registerForEvent: (id, participant) => {
            dispatch(requestRegisterForEvent(id, participant));
        },
        unregisterForEvent: (id, participantId) => {
            dispatch(requestUnregisterForEvent(id, participantId));
        },
    };
};

const VisibleEvent = connect(
	mapStateToEventProps,
	mapFetchEventToProps,
)(Event);

export default VisibleEvent;