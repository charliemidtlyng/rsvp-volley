package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface EventService {

    Event createEvent(Event event);

    Event addParticipantToEvent(Long eventId, Participant p)

    Event removeParticipantFromEvent(Long eventId, Long participantId)

    List<Event> findAllEvents()

    List<Event> findUpcomingEvents()

    Event findEventById(Long eventId)

    void deleteEvent(Long eventId)
}