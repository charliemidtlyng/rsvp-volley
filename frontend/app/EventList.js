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
                    this.setState({
                        events: events,
                        loading: false
                    });
                }.bind(this));
    },

    render: function () {
        var events = this.state.events.map(function (event) {

            return <Link to="event" params={event} key={event.id}>
                <div className="col-xs-4">
                    <h2>{event.subject}</h2>
                    <h5><strong>Start:</strong> {Utils.formatDateTime(event.startTime)}</h5>
                    <div><strong>Til:</strong> {Utils.formatDateTime(event.endTime)}</div>
                    <div><strong>Sted:</strong> {event.location}</div>
                    <div><strong>Påmelding åpner:</strong> {Utils.formatDateTime(event.regStart)}</div>
                </div>
            </Link>;
        }.bind(this));
        return (
                <div className="EventList row">
                    {events}
                </div>
        );
    }
});

module.exports = EventList;