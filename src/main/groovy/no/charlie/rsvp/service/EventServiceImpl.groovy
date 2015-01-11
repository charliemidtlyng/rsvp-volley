package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.History
import no.charlie.rsvp.domain.Participant
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.repository.EventRepository
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static no.charlie.rsvp.domain.History.Change
import static no.charlie.rsvp.domain.History.Change.Register
import static no.charlie.rsvp.domain.History.Change.Unregister

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("eventService")
class EventServiceImpl implements EventService {

    @Autowired EventRepository eventRepository

    Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    Event addParticipantToEvent(Long eventId, Participant p) {
        Event event = eventRepository.findWithPreFetch(eventId);
        validateEventIsOpen(event)
        p.setEvent(event)
        event.participants.add(p)
        event.updateParticipants()
        History history = createHistory(event, p, Register)
        event.history.add(history)
        return eventRepository.save(event)
    }

    Event removeParticipantFromEvent(Long eventId, Long participantId) {
        Event event = eventRepository.findWithPreFetch(eventId);
        validateEventIsOpen(event)
        Participant p = event.participants.find {
            it.id == participantId
        }
        event.participants.remove(p)
        event.updateParticipants()
        History history = createHistory(event, p, Unregister)
        event.history.add(history)
        return eventRepository.save(event)
    }

    List<Event> findAllEvents() {
        return eventRepository.findAll()
    }

    List<Event> findUpcomingEvents() {
        return eventRepository.findByEndTimeGreaterThan(new DateTime())
    }

    Event findEventById(Long eventId) {
        return eventRepository.findWithPreFetch(eventId)
    }

    void deleteEvent(Long eventId) {
        eventRepository.delete(eventId)
    }

    private static void validateEventIsOpen(Event event) {
        def currentTime = DateTime.now();
        if (currentTime.isBefore(event.regStart) || currentTime.isAfter(event.regEnd)) {
            def regStart = event.regStart?.toString('yyyy-MM-dd HH:mm')
            def regEnd = event.regEnd?.toString('yyyy-MM-dd HH:mm')
            throw new RsvpBadRequestException("Kan ikke meldes på/av nå. Registrering åpner $regStart  og avsluttes $regEnd")
        }
    }

    private static History createHistory(Event event, Participant p, Change change) {
        new History(
                event: event,
                change: change,
                details: p.name
        )
    }


}
