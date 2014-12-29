package no.charlie.rsvp.repository

import no.charlie.rsvp.domain.Event

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface EventRepositoryCustom {
    Event findWithPreFetch(Long eventId)
    List<Event> findAllWithPreFetch()
}
