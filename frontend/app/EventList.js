
var React = require('react');
var EventStore = require('./EventStore');
var EventImage = require('./EventImage');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var Utils = require('./Utils');
var classNames = require('classnames');

var ShowHide = React.createClass({
    render: function(){
        var btnClasses = classNames({
            'btn': true,
            'btn-default': !this.props.visibleHistory,
            'btn-danger': this.props.visibleHistory
        });
        var text = this.props.visibleHistory ? 'Skjul historikk' : 'Vis historikk';
        return <div className="margin-bottom-10"> <button className={btnClasses} onClick={this.props.toggleShowHide}>{text}</button> </div>
    }
});

var EventList = React.createClass({
    getInitialState: function () {
        return {
            upcomingEvents: [],
            oldEvents: [],
            loading: true,
            visibleHistory: false
        };
    },
    componentDidMount: function () {
        EventStore.getEvents()
                .then(function (events) {
                    var oldEvents = events.filter(this.filterOldEvents);
                    var upcomingEvents = events.filter(this.filterUpcomingEvents);
                    oldEvents.sort(Utils.sortByTimestampDesc);
                    upcomingEvents.sort(Utils.sortByTimestampAsc);
                    this.setState({
                        upcomingEvents: upcomingEvents,
                        oldEvents: oldEvents,
                        loading: false
                    });
                }.bind(this));
    },
    toggleShowHide: function() {
        this.setState({
            visibleHistory: !this.state.visibleHistory
        });
    },
    filterOldEvents: function(event) {
        return Utils.isOldEvent(event);
    },
    filterUpcomingEvents: function(event) {
        return !Utils.isOldEvent(event);
    },
    mapEvents: function(events) {
        return events.map(function (event) {
            var classes = classNames({
                'old-event': Utils.isOldEvent(event),
                'event': true,
                'col-xs-12': true,
                'col-md-9': true,
                'clearfix': true
            });
            return <Link to={`/event/${event.id}`} className={classes} key={event.id}>
                <div >
                    <h6 className="margin-bottom-0"><EventImage event={event} />{event.subject}</h6>
                    <h2 className='margin-top-10'><span>{event.location}</span> <span className="gray">({Utils.timeStampToDate(event.startTime)} {Utils.formatDateTime(event.startTime, 'dd. MMMM')})</span></h2>
                    <div><strong>Start:</strong> {Utils.formatDateTime(event.startTime, 'HH:mm')}</div>
                    <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart, 'dd. MMMM (HH:mm)')}</div>
                </div>
            </Link>;
        }.bind(this));
    },
    render: function () {

        var oldEvents = this.state.visibleHistory ? this.mapEvents(this.state.oldEvents) : [];
        var upcomingEvents = this.mapEvents(this.state.upcomingEvents);
        return (
                <div className="eventList row margin-top-50 ">
                    <ShowHide visibleHistory={this.state.visibleHistory} toggleShowHide={this.toggleShowHide} />
                    {upcomingEvents}
                    {oldEvents}
                </div>
        );
    }
});

module.exports = EventList;