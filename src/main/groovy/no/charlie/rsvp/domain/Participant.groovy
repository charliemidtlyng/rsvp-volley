package no.charlie.rsvp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import no.charlie.rsvp.domain.Annotations.GeneratedId
import no.charlie.rsvp.domain.Annotations.ManyToOneLazyNoCascade
import no.charlie.rsvp.domain.Annotations.PDateTime
import no.charlie.rsvp.domain.Annotations.RsvpEntity
import org.joda.time.DateTime

import static org.joda.time.DateTime.now

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class Participant {
    @GeneratedId Long id
    @ManyToOneLazyNoCascade @JsonBackReference Event event
    String name
    String email
    String phoneNumber
    Boolean reserve = false
    @PDateTime DateTime timestamp = now()
}
