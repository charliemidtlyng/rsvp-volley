/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');

var Route = Router.Route,
        DefaultRoute = Router.DefaultRoute,
        NotFoundRoute = Router.NotFoundRoute,
        RouteHandler = Router.RouteHandler,
        Link = Router.Link;

var App = React.createClass({
    render: function () {
        return (
            <div className="App">
                <div className="navbar navbar-default navbar-fixed-top">
                    <div className="container">
                        <div className="navbar-header">
                            <a href="/" className="navbar-brand">RSVP-app</a>
                            <button className="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                            </button>
                        </div>
                        <div className="navbar-collapse collapse" id="navbar-main">
                            <ul className="nav navbar-nav">
                                <li>
                                    <Link to="/">Hendelser</Link>
                                </li>
                                <li>
                                    <Link to="new">Ny hendelse</Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div className="container">
                    <RouteHandler/>
                </div>
            </div>
        );
    }
});

module.exports = App;