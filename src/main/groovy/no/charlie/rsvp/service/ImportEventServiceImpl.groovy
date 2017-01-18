package no.charlie.rsvp.service

import groovyx.net.http.HTTPBuilder
import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.utils.DateUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.springframework.stereotype.Service

import static no.charlie.rsvp.domain.Event.EventSubType.Match
import static no.charlie.rsvp.utils.DateUtils.toDateWithNameOfDayTimeStamp

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("importEventService")
class ImportEventServiceImpl implements ImportEventService {

    def datePattern = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")

    @Override
    List<Event> createEventsByTournamentAndTeamId(Long tournamentId, Long teamId, String subject, int maxNumber) {
        String baseUrl = 'https://wp.nif.no'
        def url = "${baseUrl}/PageCalendarTeamDetail.aspx?tournamentId=${tournamentId}&teamId=${teamId}&number=all"
        def http = new HTTPBuilder(url)
        def html = http.get([:])

        List<Event> events = html."**".findAll { it.name().contains("TR") }.
                findAll { !(it.toString().startsWith('Dato') || it.toString().startsWith('Siste')) }
                .collect { tr ->
            DateTime startTime = DateTime.parse("${tr.TD[0].text().trim()} ${tr.TD[1].text().trim()}", datePattern)
            DateTime endTime = startTime.plusHours(1)
            String homeTeam = tr.TD[3].text().trim()
            String awayTeam = tr.TD[4].text().trim()
            String opponent = homeTeam.contains("Bekk") ? awayTeam : homeTeam
            String location = tr.TD[5].text().trim()
            String locationUrl = baseUrl + tr.TD[5].A.@href

            createEventByTableRow(startTime, endTime, location, opponent, locationUrl, subject, maxNumber)
        }

        events
    }

    private static Event createEventByTableRow(DateTime start,
                                               DateTime end,
                                               String location,
                                               String opponent,
                                               String locationUrl,
                                               String subject,
                                               int maxNumber) {
        Event event = new Event()
        event.startTime = start
        event.endTime = end
        event.regStart = DateTime.now()
        event.regEnd = end
        event.location = location
        event.subject = subject
        event.eventSubType = Match
        event.maxNumber = maxNumber
        event.creator = "CM"
        def meetupTime = toDateWithNameOfDayTimeStamp(event.startTime.minusMinutes(30))
        event.description = "Oppmøte ${meetupTime} på ${location}. \n Kamp mot ${opponent}. \n Husk leggskinner og svart drakt! \n\n Beskrivelse og kart til banen: ${locationUrl}"

        event
    }
}
