package no.charlie.rsvp.service

import no.charlie.rsvp.domain.*
import no.charlie.rsvp.domain.Event.EventSubType
import no.charlie.rsvp.domain.Event.EventType
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.repository.EventRepository
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

import static no.charlie.rsvp.domain.History.Change
import static no.charlie.rsvp.domain.History.Change.Register
import static no.charlie.rsvp.domain.History.Change.Unregister
import static org.joda.time.DateTime.now

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("eventService")
class EventServiceImpl implements EventService {

    @Autowired EventRepository eventRepository
    @Autowired MailService mailService
    @Autowired SmsService smsService

    Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event)
        return savedEvent
    }

    Event addParticipantToEvent(Long eventId, Participant p) {
        Event event = eventRepository.findWithPreFetch(eventId)
        validateEventIsOpen(event)
        p.setEvent(event)
        event.participants.add(p)
        event.updateParticipants(p)
        History history = createHistory(event, p, Register)
        event.history.add(history)
        return eventRepository.save(event)
    }

    Event removeParticipantFromEvent(Long eventId, Long participantId) {
        Event event = eventRepository.findWithPreFetch(eventId)
        validateEventIsOpen(event)
        Participant p = event.participants.find {
            it.id == participantId
        }
        def shouldNotifyNextParticipant = isParticipantStatusChanging(event, p)
        event.participants.remove(p)
        event.updateParticipants()

        event.history.add(createHistory(event, p, Unregister))
        if (shouldNotifyNextParticipant && !event.hasManualLineUp()) {
            notifyNextParticipant(event)
        }
        return eventRepository.save(event)
    }

    static boolean isParticipantStatusChanging(Event event, Participant participant) {
        if (participant.reserve) {
            return false
        }
        return event.participants.size() > event.maxNumber
    }

    List<Event> findAllEvents(EventType eventType, EventSubType eventSubType) {
        return eventRepository
                .findAll()
                .findAll(filterByEventType(eventType, eventSubType))
    }

    List<Event> findUpcomingEvents(EventType eventType, EventSubType eventSubType) {
        return eventRepository
                .findByEndTimeGreaterThan(new DateTime())
                .findAll(filterByEventType(eventType, eventSubType))
    }

    Event findEventById(Long eventId) {
        return eventRepository.findWithPreFetch(eventId)
    }

    void deleteEvent(Long eventId) {
        eventRepository.delete(eventId)
    }

    Event confirmLineup(Long eventId, Map lineupMap) {
        Event event = eventRepository.findWithPreFetch(eventId)
        if (!event.hasManualLineUp()) {
            throw new RsvpBadRequestException('Ikke lov å endre på uttak for denne hendelsen')
        }
        event.participants.each {
            it.reserve = lineupMap.get(it.id.toString())
        }
        eventRepository.save(event)
    }

    @Async
    void notifyNextParticipant(Event event) {
        Participant newAttender = event.participants.findAll {
            !it.reserve
        }.last()
        mailService.sendMail(newAttender, event)
        smsService.sendSms(newAttender, '[BEKK-Fotball] Du er flyttet fra reservelisten til paameldtlisten! PS:meld deg av dersom du ikke kan stille. -Charlie')
    }


    private static void validateEventIsOpen(Event event) {
        def currentTime = now()
        if (currentTime.isBefore(event.regStart) || currentTime.isAfter(event.regEnd)) {
            throw new RsvpBadRequestException("Kan ikke meldes på/av nå!")
        }
    }

    private static History createHistory(Event event, Participant p, Change change) {
        new History(
                event: event,
                change: change,
                details: p.name
        )
    }

    private static Closure filterByEventType(EventType eventType, EventSubType eventSubType) {

        return { Event event ->
            def eventTypeFilter = eventType ? event.eventType == eventType : true
            def eventSubTypeFilter = eventSubType ? event.eventSubType == eventSubType : true
            eventTypeFilter && eventSubTypeFilter
        }
    }
}
