package no.charlie.rsvp.service

import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.utils.DateUtils
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
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
            .withLocale(new Locale('nb', 'no'))
            .withZone(DateTimeZone.forID('Europe/Oslo'))

    @Override
    List<Event> createEventsByTournamentAndTeamId(Long tournamentId, Long teamId, String subject, int maxNumber) {
        String baseUrl = 'https://wp.nif.no'
        def url = "${baseUrl}/PageCalendarTeamDetail.aspx?tournamentId=${tournamentId}&teamId=${teamId}&number=all"
        def http = new HTTPBuilder(url)
        def html = http.get([:])

        def javascriptString = html."**".findAll { it.name() == "SCRIPT" }
                .findAll{ it.toString().contains("BEKK") || it.toString().contains("Bekk") }
                .collect {it.toString()}.get(0)
        def parsetDel1 = javascriptString.substring(javascriptString.lastIndexOf("var options = {")+ "var options = {".length()-1)
        def parsetDel2 = parsetDel1.substring(0, parsetDel1.indexOf(";"))
        Map<String, List> slurper = new JsonSlurper().parseText(parsetDel2)
        List data = slurper.get("data")
        def index = 0
        
        List<Event> events = data.collect { List<String> elements->
            if(index != 0 && elements.size() > 0) {

                DateTime startTime = DateTime.parse("${removeHtmlTags(elements.get(0))} ${removeHtmlTags(elements.get(1))}", datePattern)
                DateTime endTime = startTime.plusHours(1)
                String homeTeam = removeHtmlTags(elements.get(3))
                String awayTeam = removeHtmlTags(elements.get(4))
                String opponent = homeTeam.contains("Bekk") ? awayTeam : homeTeam
                String location = removeHtmlTags(elements.get(5))
                String locationHref = new XmlSlurper().parseText(elements.get(5)).'**'.findAll {it.name() == 'a'}.first().attributes().href
                String locationUrl = baseUrl + locationHref
                index++;
                return createEventByTableRow(startTime, endTime, location, opponent, locationUrl, subject, maxNumber)
            }
            index++;
            return null
        }.findAll {
            it != null
        }

        events
    }

    static String removeHtmlTags(String s) {
        s.replaceAll("\\<.*?>","")
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
        event.description = "Oppmøte ${meetupTime} på ${location}. \n Kamp mot ${opponent}. \n\n Beskrivelse og kart til banen: ${locationUrl}"

        event
    }
}
