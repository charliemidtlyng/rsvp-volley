package no.charlie.rsvp.service

import no.charlie.rsvp.domain.*
import no.charlie.rsvp.domain.Event.EventSubType
import no.charlie.rsvp.domain.Event.EventType

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface OtpService {

    Otp resolveOtpForEventId(Long eventId)

    void validateOtp(long eventId, String password)
}