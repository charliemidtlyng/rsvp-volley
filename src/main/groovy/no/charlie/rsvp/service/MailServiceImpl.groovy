package no.charlie.rsvp.service

import com.sendgrid.SendGrid
import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant
import org.springframework.stereotype.Service

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("mailService")
class MailServiceImpl implements MailService {

    @Override
    void sendMail(Participant participant, Event event) {
        if (participant.email) {
            SendGrid sendgrid = new SendGrid(System.getenv("SENDGRID_USERNAME"), System.getenv("SENDGRID_PASSWORD"))
            SendGrid.Email email = new SendGrid.Email()
            email.addTo(participant.email)
            email.setFrom("charlie.midtlyng@bekk.no")
            email.setSubject("[BEKK-Fotball] - ${event.subject}")
            email.setText("Noen har meldt seg av og du har dermed fått plass på ${event.subject} som starter ${event.startTime?.toString('yyyy-MM-dd HH:mm')}. \n -Charlie")

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
