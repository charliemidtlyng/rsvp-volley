import { take, put, call, fork, select, takeEvery } from 'redux-saga/effects'
import fetch from 'isomorphic-fetch'
import * as actions from './actions'
import { allEventsSelector } from './selectors'
import regeneratorRuntime from "regenerator-runtime";
window.regeneratorRuntime = regeneratorRuntime;


var API = '/api/events';

export function fetchPostsApi(event) {
    return fetch(API)
            .then(response => response.json() )
            .then(json => json )
}

export function* fetchPosts(event) {
  yield put( actions.requestEvents(event) )
  const posts = yield call(fetchPostsApi, event)
  yield put( actions.receiveEvents(event, posts) )
}

export default function* root() {
  yield takeEvery(actions.REFRESH_EVENTS, fetchPosts);
}
