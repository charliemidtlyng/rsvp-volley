package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Participant

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface SmsService {

    void sendSms(Participant participant, String message)

}