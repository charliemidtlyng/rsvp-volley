/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var Link = Router.Link;
var Utils = require('./Utils');

var ShowHide = React.createClass({
    render: function(){
        var cx = React.addons.classSet;
        var btnClasses = cx({
            'btn': true,
            'btn-default': !this.props.visibleHistory,
            'btn-danger': this.props.visibleHistory
        });
        var text = this.props.visibleHistory ? 'Skjul historikk' : 'Vis historikk';
        return <div className="margin-bottom-10"> <button className={btnClasses} onClick={this.props.toggleShowHide}>{text}</button> </div>
    }
});


var EventList = React.createClass({
    mixins: [Router.Navigation, Router.State],
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
        var cx = React.addons.classSet;
        return events.map(function (event) {
            var classes = cx({
                'old-event': Utils.isOldEvent(event),
                'event': true,
                'col-xs-12': true,
                'col-md-9': true,
                'clearfix': true
            });
            return <Link to="event" className={classes} params={event} key={event.id}>
                <div >
                    <h6 className="margin-bottom-0">{event.subject}</h6>
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