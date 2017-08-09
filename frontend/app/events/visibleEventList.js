import EventList from './list'
import { toggleOldEvents, refreshEvents } from './actions'
import { connect } from 'react-redux';
// maps redux store state to components
const mapStateToEventListProps = (state) => {
	return {...state.events.allEvents, visibleHistory: state.events.toggleOldEvents.visibleHistory };
};

// // maps redux store dispatch to list of components
const mapToggleOldToProps = (dispatch) => {
	return {
		toggleOldEvents: () => {
			dispatch(toggleOldEvents());
		},
        fetchEvents: () => {
            dispatch(refreshEvents());
        }
	};
};

const VisibleEventList = connect(
	mapStateToEventListProps,
	mapToggleOldToProps,
)(EventList);

export default VisibleEventList;