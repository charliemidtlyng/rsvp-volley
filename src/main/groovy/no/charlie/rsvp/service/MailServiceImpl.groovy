package no.charlie.rsvp.service

import com.sendgrid.SendGrid
import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant
import org.joda.time.DateTimeZone
import org.slf4j.Logger
import org.springframework.stereotype.Service

import static no.charlie.rsvp.domain.Event.EventSubType.Match
import static no.charlie.rsvp.domain.Event.EventSubType.Training
import static no.charlie.rsvp.utils.DateUtils.*
import static org.slf4j.LoggerFactory.getLogger

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("mailService")
class MailServiceImpl implements MailService {
    private static final Logger LOGGER = getLogger(MailService)

    @Override
    void sendMail(Participant participant, Event event) {
        if (participant.email) {
            SendGrid sendgrid = new SendGrid(System.getenv("SENDGRID_USERNAME"), System.getenv("SENDGRID_PASSWORD"))
            SendGrid.Email email = new SendGrid.Email()
            email.addTo(participant.email)
            email.setFrom("charlie.midtlyng@gmail.com")
            email.setSubject("[BEKK-Fotball] - ${event.subject}")
            def startTimeString = event.startTime?.
                    withZone(DateTimeZone.forID('Europe/Oslo'))?.
                    toLocalDateTime()?.toString('yyyy-MM-dd HH:mm')
            email.setText("Noen har meldt seg av og du har dermed fått " +
                    "plass på ${event.subject} som starter ${startTimeString}. \n -Charlie")

            try {
                SendGrid.Response response = sendgrid.send(email)
                if (!response.status) {
                    sendMailToAdmin(participant, event)
                } else {
                    println("Epost sendt til ${participant.name} - ${participant.email}")
                }
            } catch (Exception e) {
                println(e)
            }

        } else {
            sendMailToAdmin(participant, event)
            println("Missing email address for ${participant.name}")
        }
    }

    @Override
    void sendEventNotification(Event event) {
        String mailText = generateNotificationMailText(event)

        SendGrid sendgrid = new SendGrid(System.getenv("SENDGRID_USERNAME"), System.getenv("SENDGRID_PASSWORD"))
        SendGrid.Email email = new SendGrid.Email()
        email.addTo("fotball@bekk.no")
        email.setFrom("charlie.midtlyng@gmail.com")
        email.setSubject(generateSubject(event))
        email.setHtml(mailText)
        try {
            sendgrid.send(email)
        } catch (Exception e) {
            println(e)
        }

    }

    String generateSubject(Event event) {
        "[BEKK-Fotball] ${event.subject} på ${toDayOfWeek(event.startTime)}"
    }

    private String generateNotificationMailText(Event event) {
        String mailText = ''

        def startDay = toDayOfWeek(event.startTime)
        def startTime = toTime(event.startTime)
        def endTime = toTime(event.endTime)
        def url = "http://paamelding.herokuapp.com/#/event/${event.id}"
        def regStartDay = toDayOfWeek(event.regStart)
        def regStartTime = toTime(event.regStart)
        if (event.eventSubType == Training) {
            mailText = """
            <p>Hei!</p>
            <p>Ny trening på ${startDay}. Påmelding åpner ${regStartDay} kl. ${regStartTime}!</p>
            <ul>
            <li>Tid: <b>${startDay} ${startTime}-${endTime}</b> (oppmøte ${toTime(event.startTime.minusMinutes(15))}!)</li>
            <li>Sted: ${event.location}</li>
            <li>Påmelding åpner: ${toDate(event.regStart)} kl. ${regStartTime}</li>
            </ul>
            <p>Info og påmelding: <a href='${url}'>${url}</a></p>

            <p>NB: Det er mange avmeldinger i siste liten. Tenk på dine kollegaer og meld deg av senest 12:00 på ${startDay}!</p>
            <p><b>CHARLIE</b></p>
            <p><b>99402316</b></p>"""
        } else if (event.eventSubType == Match) {
            mailText = """<p>Hei!</p>
            <p>Ny kamp på ${startDay}. Påmelding er åpen!</p>
            <ul>
            <li>Tid: <b>${startDay} ${startTime}-${endTime}</b> (oppmøte ${toTime(event.startTime.minusMinutes(30))}!)</li>
            <li>Sted: ${event.location}</li>
            </ul>
            <p>Info og påmelding: <a href='${url}'>${url}</a></p>

            <p>NB: Uttak skjer innen rimelig tid!</p>
            <p><b>CHARLIE</b></p>
            <p><b>99402316</b></p>"""
        }

        mailText
    }
    
    static void sendMailToAdmin(Participant p, event) {
        SendGrid sendgrid = new SendGrid(System.getenv("SENDGRID_USERNAME"), System.getenv("SENDGRID_PASSWORD"))

        SendGrid.Email email = new SendGrid.Email()
        email.addTo("charlie.midtlyng@bekk.no")
        email.setFrom("charlie.midtlyng@bekk.no")
        email.setSubject("[BEKK-Fotball] - ${event.subject}")
        email.setText("${p.name} har gått fra reservelisten til påmeldtlisten, men ikke registrert eposten sin må derfor få beskjed!")
        try {
            sendgrid.send(email)
        } catch (Exception e) {
            println(e)
        }
    }
}
