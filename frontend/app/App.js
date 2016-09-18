var React = require('react');
var EventStore = require('./EventStore');
var ReactBootstrap = require('react-bootstrap');
var Navbar = ReactBootstrap.Navbar;
var Nav = ReactBootstrap.Nav;
var NavItem = ReactBootstrap.NavItem;

var App = React.createClass({
    render: function () {
        return (
            <div className="App">
                <Navbar fixedTop={true} >
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#"><img src="/css/fotball_logo.png" /></a>
                        </Navbar.Brand>
                        <Navbar.Toggle />
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav eventKey={1}>
                            <NavItem key={1} href="/#">Hendelser</NavItem>
                            <NavItem key={2} href="#/event/new">Ny hendelse</NavItem>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <div className="container">
                    {this.props.children}
                </div>
            </div>
        );
    }
});

module.exports = App;