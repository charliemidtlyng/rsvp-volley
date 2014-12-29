package no.charlie.rsvp.domain

import no.charlie.rsvp.domain.Annotations.GeneratedId
import no.charlie.rsvp.domain.Annotations.OneToManySubselect
import no.charlie.rsvp.domain.Annotations.PDateTime
import no.charlie.rsvp.domain.Annotations.RsvpEntity
import org.joda.time.DateTime

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class Event {
    @GeneratedId Long id
    @PDateTime DateTime start
    @PDateTime DateTime end
    @PDateTime DateTime regStart
    @PDateTime DateTime regEnd

    String creator
    String location
    String subject
    String description
    Integer maxNumber = Integer.MAX_VALUE

    @OneToManySubselect Set<Participant> participants = []
    @OneToManySubselect Set<History> history = []

    Event fetchMainCollections() {
        participants.size()
        history.size()
        this
    }

    Event updateParticipants() {
        participants.eachWithIndex { Participant entry, int index ->
            entry.reserve = index >= maxNumber
        }
        this
    }

}
