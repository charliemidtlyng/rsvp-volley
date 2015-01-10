package no.charlie.rsvp.domain

import no.charlie.rsvp.domain.Annotations.GeneratedId
import no.charlie.rsvp.domain.Annotations.OneToManySubselect
import no.charlie.rsvp.domain.Annotations.PDateTime
import no.charlie.rsvp.domain.Annotations.RsvpEntity
import org.joda.time.DateTime

import javax.persistence.Column

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class Event {
    @GeneratedId Long id
    @PDateTime DateTime startTime
    @PDateTime DateTime endTime
    @PDateTime DateTime regStart
    @PDateTime DateTime regEnd

    String creator
    String location
    String subject
    @Column(length = 2048) String description
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
