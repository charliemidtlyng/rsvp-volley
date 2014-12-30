/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var NewEvent = require('./NewEvent');
var Event = require('./Event');
var App = require('./App')
var Router = require('react-router');
var Bootstrap = require('react-bootstrap');
var EventList = require('./EventList');

var Route = Router.Route,
        DefaultRoute = Router.DefaultRoute,
        NotFoundRoute = Router.NotFoundRoute,
        RouteHandler = Router.RouteHandler,
        Link = Router.Link;

var NotFound = React.createClass({
    render: function () {
        return <h2>Not found</h2>;
    }
});

var routes = (
        <Route handler={App}>
            <DefaultRoute handler={EventList}/>
            <Route name="new" path="event/new" handler={NewEvent}/>
            <Route name="event" path="event/:id" handler={Event}/>
            <NotFoundRoute handler={NotFound}/>
        </Route>
);

Router.run(routes, function (Handler) {
    React.render(<Handler/>, document.getElementById('react-content'));
});