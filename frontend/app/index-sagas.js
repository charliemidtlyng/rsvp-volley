import EventsSaga from './events/sagas'
import EventSaga from './event/sagas'
export default function* IndexSaga () {
  yield [
    EventsSaga(),
    EventSaga(),
  ]
}
