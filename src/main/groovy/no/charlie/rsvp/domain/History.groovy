package no.charlie.rsvp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import no.charlie.rsvp.domain.Annotations.*
import org.joda.time.DateTime

import static org.joda.time.DateTime.now

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class History {
    @GeneratedId Long id
    @ManyToOneLazyNoCascade @JsonBackReference('history') Event event
    @PDateTime DateTime timestamp = now()
    @PEnum Change change
    String details

    public static enum Change {
        Register,
        Unregister,
        Update
    }
}
