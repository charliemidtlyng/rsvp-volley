export const REFRESH_EVENTS = 'REFRESH_EVENTS';
export const REFRESH_OLD_EVENTS = 'REFRESH_OLD_EVENTS';
export const REQUEST_EVENTS = 'REQUEST_EVENTS';
export const REQUEST_OLD_EVENTS = 'REQUEST_OLD_EVENTS';
export const RECEIVE_EVENTS = 'RECEIVE_EVENTS';
export const RECEIVE_OLD_EVENTS = 'RECEIVE_OLD_EVENTS';
export const TOGGLE_OLD_EVENTS = 'TOGGLE_OLD_EVENTS';

export function refreshEvents() {
  return {
    type: REFRESH_EVENTS
  }
}

export function refreshOldEvents() {
  return {
    type: REFRESH_OLD_EVENTS
  }
}

export function toggleOldEvents(event, events) {
  return {
    type: TOGGLE_OLD_EVENTS,
    event,
    events
  }
}

export function requestEvents(event) {
  return {
    type: REQUEST_EVENTS,
    event
  }
}

export function requestOldEvents(event) {
  return {
    type: REQUEST_OLD_EVENTS,
    event
  }
}

export function receiveEvents(event, events) {
  return {
    type: RECEIVE_EVENTS,
    event,
    events,
    receivedAt: Date.now()
  }
}
export function receiveOldEvents(event, events) {
  return {
    type: RECEIVE_OLD_EVENTS,
    event,
    events,
    receivedAt: Date.now()
  }
}
