package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import spock.lang.Specification

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class ImportEventServiceImplTest extends Specification {

    ImportEventService importEventService = new ImportEventServiceImpl()

    def 'should import from url and create events'() {
        when:
            List<Event> events = importEventService.createEventsByTournamentAndTeamId(371313, 723658, 'Kamp 1. laget', 8)
        then:
            events.size() == 11
            events.findAll { it.subject == "Kamp 1. laget" }.size() == 11
            events.findAll { it.maxNumber == 8 }.size() == 11
    }
}
