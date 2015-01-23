/** @jsx React.DOM */
var React = require('react/addons');
var EventStore = require('./EventStore');
var Router = require('react-router');
var ReactBootstrap = require('react-bootstrap');
var Navbar = ReactBootstrap.Navbar;
var Nav = ReactBootstrap.Nav;
var NavItem = ReactBootstrap.NavItem;

var Route = Router.Route,
        DefaultRoute = Router.DefaultRoute,
        NotFoundRoute = Router.NotFoundRoute,
        RouteHandler = Router.RouteHandler,
        Link = Router.Link;

var App = React.createClass({
    render: function () {
        return (
            <div className="App">
                <Navbar id="main-nav" brand={<a href="#">RSVP-app</a>} toggleNavKey={1} fixedTop={true} >
                    <Nav eventKey={1}>
                      <NavItem key={1} href="/#">Hendelser</NavItem>
                      <NavItem key={2} href="#/event/new">Ny hendelse</NavItem>
                    </Nav>
                </Navbar>
                <div className="container">
                    <RouteHandler/>
                </div>
            </div>
        );
    }
});

module.exports = App;