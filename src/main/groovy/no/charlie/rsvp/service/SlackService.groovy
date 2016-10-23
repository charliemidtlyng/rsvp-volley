package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface SlackService {

    void sendEventNotification(Event event)

}