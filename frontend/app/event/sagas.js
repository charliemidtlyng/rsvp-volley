import {
    take,
    put,
    call,
    fork,
    select,
    takeLatest,
    takeEvery,
    dispatch
} from "redux-saga/effects";
import fetch from "isomorphic-fetch";
import * as actions from "./actions";
import regeneratorRuntime from "regenerator-runtime";
window.regeneratorRuntime = regeneratorRuntime;

var API = "/api/events/";

export function fetchEventApi(eventId) {
    return fetch(API + eventId)
        .then(response => response.json())
        .then(json => json);
}

export function* fetchEvent(action) {
    yield put(actions.requestEvent(action.eventId));
    const event = yield call(fetchEventApi, action.eventId);
    yield put(actions.receiveEvent(action.eventId, event));
}

export function registerForEventApi(action) {
    var options = {
        method: "POST",
        body: JSON.stringify(action.participant),
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        }
    };
    return fetch(API + action.eventId + "/register", options).then(response => {
        if (response.status >= 200 && response.status <= 300) {
            return response.json();
        }
        return response.text().then(errorText => {
            throw new Error(errorText);
        });
    });
}
export function* registerForEvent(action) {
    try {
        const response = yield call(registerForEventApi, action);
        yield put(actions.selectEvent(action.eventId))
    } catch (errorText) {
        yield put.resolve(
            actions.receiveRegisterForEvent(action.eventId, {
                error: errorText
            })
        );
    }
}

function* startup() {
    yield takeEvery(actions.SELECT_EVENT, fetchEvent);
    yield takeEvery(actions.REQUEST_REGISTER_FOR_EVENT, registerForEvent);
}

export default startup;
