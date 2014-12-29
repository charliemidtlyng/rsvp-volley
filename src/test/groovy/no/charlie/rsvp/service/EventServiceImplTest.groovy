package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.History
import no.charlie.rsvp.domain.Participant
import no.charlie.rsvp.repository.EventRepository
import org.joda.time.DateTime
import spock.lang.Specification

import javax.ws.rs.BadRequestException

import static org.joda.time.DateTime.now

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class EventServiceImplTest extends Specification {

    def eventRepository = Mock(EventRepository)
    EventService eventService
    def currentTimePlus1 = now().plusDays(1)
    def currentTimePlus2 = now().plusDays(2)
    def currentTimeMinus1 = now().minusDays(1)
    def currentTimeMinus2 = now().minusDays(2)

    def setup() {
        eventService = new EventServiceImpl(
                eventRepository: eventRepository
        )
    }

    def "should fail when attending too early"() {
        when:
            eventService.addParticipantToEvent(1L, new Participant(name: "x"))
        then:
            eventRepository.findWithPreFetch(_) >> new Event(
                    startTime: currentTimePlus2,
                    regStart: currentTimePlus1,
                    endTime: currentTimePlus2,
                    regEnd: currentTimePlus2
            )
            thrown(BadRequestException)
    }

    def "should fail when attending too late"() {
        when:
            eventService.addParticipantToEvent(1L, new Participant(name: "x"))
        then:
            eventRepository.findWithPreFetch(_) >> new Event(
                    startTime: currentTimeMinus1,
                    regStart: currentTimeMinus2,
                    endTime: currentTimeMinus1,
                    regEnd: currentTimeMinus1
            )
            thrown(BadRequestException)
    }

    def "should register participant to event"() {
        when:
            def event = eventService.addParticipantToEvent(1L, new Participant(name: "x"))
        then:
            eventRepository.findWithPreFetch(_) >> new Event(
                    startTime: currentTimePlus2,
                    regStart: currentTimeMinus1,
                    endTime: currentTimePlus2,
                    regEnd: currentTimePlus2,
                    participants: [],
                    history: []
            )
            eventRepository.save(_) >> {
                it.first()
            }

            event.participants.size() == 1
            event.participants.first().name == 'x'
            event.history.size() == 1
            event.history.first().change == History.Change.Register
    }

    def "should register participant to event as reserve"() {
        when:
            def event = eventService.addParticipantToEvent(1L, new Participant(name: "x"))
        then:
            eventRepository.findWithPreFetch(_) >> new Event(
                    startTime: currentTimePlus2,
                    regStart: currentTimeMinus1,
                    endTime: currentTimePlus2,
                    regEnd: currentTimePlus2,
                    participants: createParticipants(5),
                    history: [],
                    maxNumber: 5
            )
            eventRepository.save(_) >> {
                it.first()
            }

            event.participants.size() == 6
            !event.participants.first().reserve
            event.participants.last().reserve
    }

    def "should unregister participant to event"() {
        when:
            def event = eventService.removeParticipantFromEvent(1L, 1L)
        then:
            eventRepository.findWithPreFetch(_) >> new Event(
                    startTime: currentTimePlus2,
                    regStart: currentTimeMinus1,
                    endTime: currentTimePlus2,
                    regEnd: currentTimePlus2,
                    participants: createParticipants(2),
                    history: []
            )
            eventRepository.save(_) >> {
                it.first()
            }

            event.participants.size() == 1
            event.history.size() == 1
            event.history.first().change == History.Change.Unregister
    }

    def "should unregister participant to event and update an reserve"() {
        when:
            def event = eventService.removeParticipantFromEvent(1L, 1L)
        then:

            eventRepository.findWithPreFetch(_) >> {
                Set<Participant> participants = createParticipants(6)
                participants.last().reserve = true
                new Event(
                        startTime: currentTimePlus2,
                        regStart: currentTimeMinus1,
                        endTime: currentTimePlus2,
                        regEnd: currentTimePlus2,
                        participants: participants,
                        history: [],
                        maxNumber: 5
                )
            }
            eventRepository.save(_) >> {
                it.first()
            }

            event.participants.size() == 5
            !event.participants.first().reserve
            !event.participants.last().reserve
    }

    Set createParticipants(int number) {
        (1..number).collect{
            new Participant(id: it, name:"x", timestamp: currentTimeMinus1)
        }
    }
}
