package no.charlie.rsvp.service

import groovyx.net.http.HTTPBuilder
import no.charlie.rsvp.domain.Event
import org.slf4j.Logger
import org.springframework.stereotype.Service

import static groovyx.net.http.ContentType.JSON
import static no.charlie.rsvp.domain.Event.EventSubType.Match
import static no.charlie.rsvp.domain.Event.EventSubType.Training
import static no.charlie.rsvp.utils.DateUtils.toDateWithNameOfDayTimeStamp
import static no.charlie.rsvp.utils.DateUtils.toDateWithTimeStamp
import static org.slf4j.LoggerFactory.getLogger

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("slackService")
class SlackServiceImpl implements SlackService {
    private static final Logger LOGGER = getLogger(SlackService)

    String webhookUrl = 'https://hooks.slack.com'
    String matchImage = 'http://www.soccerblog.com/wp-content/uploads/2016/09/cl.jpg'
    String trainingImage = 'http://pub.tv2.no/multimedia/TV2/archive/00560/pondus_illustrasjon_560016i.jpg'

    @Override
    void sendEventNotification(Event event) {
        LOGGER.info("Sending notification to slack")
        sendNotification(event)

    }

    private void sendNotification(Event event) {
        String webhookPath = System.getenv("WEBHOOK_PATH")
        String notification = generateNotificationText(event)

        def http = new HTTPBuilder(webhookUrl)
        http.ignoreSSLIssues()
        def eventImageUrl = event.eventSubType == Match ? matchImage : trainingImage
        http.post(
                path: webhookPath,
                requestContentType: JSON,
                body: [
                        text: event.subject,
                        attachments: [[
                                fallback : notification,
                                text     : notification,
                                image_url: eventImageUrl
                        ]]
                ]);

    }

    public static String generateNotificationText(Event event) {
        String notificationText = ''
        if (event.eventSubType == Training) {
            notificationText = "${event.subject} ${toDateWithNameOfDayTimeStamp(event.startTime)}. \n " +
                    "Påmelding åpner ${toDateWithNameOfDayTimeStamp(event.regStart)} \n ${eventUrl(event)}"
        } else if (event.eventSubType == Match) {
            notificationText = "${event.subject} - ${event.location} - ${toDateWithNameOfDayTimeStamp(event.startTime)}. \n Påmelding åpen " +
                    "og uttak skjer innen rimelig tid \n ${eventUrl(event)}"
        }

        notificationText
    }

    private static String eventUrl(Event event) {
        "<http://paamelding.herokuapp.com/#/event/${event.id}>"
    }
}
