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
        return (
                <div className="eventList row margin-bottom-30 margin-top-50 ">
                    {events}
                </div>
        );
    }
});

module.exports = EventList;