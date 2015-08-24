package no.charlie.rsvp.repository

import no.charlie.rsvp.domain.Otp
import org.springframework.data.repository.CrudRepository

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface OtpRepository extends CrudRepository<Otp, Long> {

    Otp findByEventId(Long eventId)
}