import regeneratorRuntime from "regenerator-runtime";
window.regeneratorRuntime = regeneratorRuntime;
var React = require('react');
var ReactDOM = require('react-dom');
var EventStore = require('./EventStore');
var NewEvent = require('./NewEvent');
var ImportEvents = require('./import/ImportEvents');

var VisibleEvent = require('./event/visibleEvent');
var EventLineup = require('./EventLineup');
var App = require('./App')
import {Router, Route, IndexRoute, hashHistory} from 'react-router'

var Bootstrap = require('react-bootstrap');

var Moment = require('moment');
var nb = require('moment/locale/nb');
var momentLocalizer = require('react-widgets/lib/localizers/moment');
momentLocalizer(Moment);


import { createStore, applyMiddleware, compose } from 'redux'
import createSagaMiddleware from 'redux-saga'
import {Provider} from 'react-redux';

import IndexReducer from './index-reducer'
import IndexSagas from './index-sagas'
import VisibleEventList from './events/visibleEventList'


const sagaMiddleware = createSagaMiddleware()

var NotFound = React.createClass({
    render: function () {
        return <h2>Not found</h2>;
    }
});

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(IndexReducer, /* preloadedState, */ composeEnhancers(
    applyMiddleware(sagaMiddleware)
  ));


sagaMiddleware.run(IndexSagas)
var routes = (
        <Provider store={store}>
            <Router history={hashHistory}>
                <Route path="/" component={App}>
                    <IndexRoute name="home" component={VisibleEventList}/>
                    <Route path="event/new" component={NewEvent}/>
                    <Route path="event/import" component={ImportEvents}/>
                    <Route path="event/:id" component={VisibleEvent.default}/>
                    <Route path="event/:id/lineup" component={EventLineup}/>
                    <Route path="*" component={NotFound}/>
                </Route>
            </Router>
        </Provider>
);

ReactDOM.render(routes, document.getElementById('react-content'));