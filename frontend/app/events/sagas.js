import { take, put, call, fork, select, takeEvery } from 'redux-saga/effects'
import fetch from 'isomorphic-fetch'
import * as actions from './actions'
import { allEventsSelector } from './selectors'
import regeneratorRuntime from "regenerator-runtime";
window.regeneratorRuntime = regeneratorRuntime;


var API_ALL = '/api/events';
var API_UPCOMING = '/api/events/upcoming';

export function fetchPostsApi(apiUrl) {
    return fetch(apiUrl)
            .then(response => response.json() )
            .then(json => json )
}

export function* fetchPosts(event) {
  yield put( actions.requestEvents(event) );
  const posts = yield call(fetchPostsApi, API_UPCOMING);
  yield put( actions.receiveEvents(event, posts) );
}

export function* fetchOldPosts(event) {
  yield put( actions.requestOldEvents(event) );
  const posts = yield call(fetchPostsApi, API_ALL);
  yield put( actions.receiveOldEvents(event, posts) );
}

export default function* root() {
  yield takeEvery(actions.REFRESH_EVENTS, fetchPosts);
  yield takeEvery(actions.REFRESH_OLD_EVENTS, fetchOldPosts);
}
