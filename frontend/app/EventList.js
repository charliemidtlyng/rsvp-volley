/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var Link = Router.Link;
var Utils = require('./Utils');

var EventList = React.createClass({
    mixins: [Router.Navigation, Router.State],
    getInitialState: function () {
        return {
            events: [],
            loading: true
        };
    },
    componentDidMount: function () {
        EventStore.getEvents()
                .then(function (events) {
                    events.sort(Utils.sortByTimestampDesc);
                    this.setState({
                        events: events,
                        loading: false
                    });
                }.bind(this));
    },

    render: function () {
        var cx = React.addons.classSet;
        var events = this.state.events.map(function (event) {
              var classes = cx({
                'old-event': Utils.isOldEvent(event)
              });
            return <Link to="event" className='col-xs-12 col-sm-4 clearfix' params={event} key={event.id}>
                <div className={classes}>
                    <h2 className='text-nowrap'>{event.subject} <small><br /> ({Utils.timeStampToDate(event.startTime)}-{Utils.formatDateTime(event.startTime, 'yyyy-MM-dd')})</small></h2>
                    <h5><strong>Start:</strong> {Utils.formatDateTime(event.startTime)}</h5>
                    <div><strong>Sted:</strong> {event.location}</div>
                    <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart)}</div>
                </div>
            </Link>;
        }.bind(this));
        return (
                <div className="eventList row margin-bottom-30">
                    <h2 className="page-header margin-bottom-0">Dette skjer!</h2>
                    {events}
                </div>
        );
    }
});

module.exports = EventList;