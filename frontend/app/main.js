var React = require('react');
var ReactDOM = require('react-dom');
var EventStore = require('./EventStore');
var NewEvent = require('./NewEvent');
var ImportEvents = require('./ImportEvents');
var Event = require('./Event');
var EventLineup = require('./EventLineup');
var App = require('./App')
var ReactRouter = require('react-router');
var Router = ReactRouter.Router;
var Bootstrap = require('react-bootstrap');
var EventList = require('./EventList');
var createHashHistory = require('react-router/node_modules/history').createHashHistory;

var Moment = require('moment');
var nb = require('moment/locale/nb');
var momentLocalizer = require('react-widgets/lib/localizers/moment');
momentLocalizer(Moment);

var Route = ReactRouter.Route,
        IndexRoute = ReactRouter.IndexRoute,
        useRouterHistory = ReactRouter.useRouterHistory;

var appHistory = useRouterHistory(createHashHistory)({queryKey: false});

var NotFound = React.createClass({
    render: function () {
        return <h2>Not found</h2>;
    }
});

var routes = (
        <Route path="/" component={App}>
            <IndexRoute name="home" component={EventList}/>
            <Route path="event/new" component={NewEvent}/>
            <Route path="event/import" component={ImportEvents}/>
            <Route path="event/:id" component={Event}/>
            <Route path="event/:id/lineup" component={EventLineup}/>
            <Route path="*" component={NotFound}/>
        </Route>
);

ReactDOM.render(<Router history={appHistory}>{routes}</Router>, document.getElementById('react-content'));
