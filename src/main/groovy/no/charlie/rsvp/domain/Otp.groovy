package no.charlie.rsvp.domain

import no.charlie.rsvp.domain.Annotations.GeneratedId
import no.charlie.rsvp.domain.Annotations.RsvpEntity

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class Otp {
    @GeneratedId Long id
    Long eventId
    String password


    static String generatePassword() {
        String.format("%04d", (int) Math.random() * 10000)
    }
}
