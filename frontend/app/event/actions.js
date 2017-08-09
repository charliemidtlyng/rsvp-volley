export const REQUEST_EVENT = 'REQUEST_EVENT'
export const RECEIVE_EVENT = 'RECEIVE_EVENT'

export const REQUEST_UNREGISTER_FOR_EVENT = 'REQUEST_UNREGISTER_FOR_EVENT'
export const RECEIVE_UNREGISTER_FOR_EVENT = 'RECEIVE_UNREGISTER_FOR_EVENT'

export const REQUEST_REGISTER_FOR_EVENT = 'REQUEST_REGISTER_FOR_EVENT'
export const RECEIVE_REGISTER_FOR_EVENT = 'RECEIVE_REGISTER_FOR_EVENT'

export const SELECT_EVENT = 'SELECT_EVENT'

export function selectEvent(eventId) {
  return {
    type: SELECT_EVENT,
    eventId
  }
}

export function requestEvent(eventId) {
  return {
    type: REQUEST_EVENT,
    eventId
  }
}

export function requestUnregisterForEvent(eventId, participantId) {
  return {
    type: REQUEST_UNREGISTER_FOR_EVENT,
    eventId,
    participantId
  }
}

export function receiveUnregisterForEvent(eventId, event, status) {
  return {
    type: RECEIVE_UNREGISTER_FOR_EVENT,
    eventId,
    event,
    status
  }
}


export function requestRegisterForEvent(eventId, participant) {
  return {
    type: REQUEST_REGISTER_FOR_EVENT,
    eventId,
    participant
  }
}

export function receiveRegisterForEvent(eventId, event, status) {
  return {
    type: RECEIVE_REGISTER_FOR_EVENT,
    eventId,
    event,
    status
  }
}

export function receiveEvent(eventId, event) {
  return {
    type: RECEIVE_EVENT,
    eventId,
    event,
    receivedAt: Date.now()
  }
}
