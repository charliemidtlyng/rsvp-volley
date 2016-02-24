package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Event.EventSubType
import no.charlie.rsvp.domain.Event.EventType
import no.charlie.rsvp.domain.Otp
import no.charlie.rsvp.domain.Participant

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface EventService {

    Event createEvent(Event event);

    Event addParticipantToEvent(Long eventId, Participant p)

    Event removeParticipantFromEvent(Long eventId, Long participantId)

    List<Event> findAllEvents(EventType eventType, EventSubType eventSubType)

    List<Event> findUpcomingEvents(EventType eventType, EventSubType eventSubType)

    Event findEventById(Long eventId)

    void deleteEvent(Long eventId)

    Event confirmLineup(Long eventId, Map longBooleanMap)

}