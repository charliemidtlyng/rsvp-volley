package no.charlie.rsvp.domain

import org.joda.time.DateTime

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class History {
    long id
    DateTime timestamp
    Change change
    Participant participant

    public static enum Change {
        Join,
        Discard,
        Update
    }
}
