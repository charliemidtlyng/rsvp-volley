package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import spock.lang.Shared
import spock.lang.Specification

import static no.charlie.rsvp.utils.DateUtils.toDateWithTimeStamp

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class SlackServiceImplTest extends Specification {

    @Shared def datePattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    SlackService slackService;

    def 'should send notification'() {
        when:
            slackService = new SlackServiceImpl()
            def event = createEvent()
            def notificationText = slackService.generateNotificationText(event)
        then:
            notificationText.contains("${event.subject} fredag ${toDateWithTimeStamp(event.startTime)}")

    }

    Event createEvent() {
        def startTime = DateTime.parse('2016-12-30 20:00:00', datePattern)
        def endTime = startTime.plusHours(1)
        def regStart = DateTime.parse('2016-12-30 21:00:00', datePattern)
        new Event(
                startTime: startTime,
                endTime: endTime,
                regStart: regStart,
                regEnd: endTime,
                subject: 'Trening',
                location: 'Vallhall',
                description: 'Fotballtrening i Vallhall.'
        )
    }
}
