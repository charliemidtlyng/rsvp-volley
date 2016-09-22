package no.charlie.rsvp.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import no.charlie.rsvp.domain.Annotations.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import javax.persistence.Column
import javax.persistence.Transient

import static no.charlie.rsvp.domain.Event.EventSubType.Match
import static no.charlie.rsvp.domain.Event.EventSubType.Training
import static no.charlie.rsvp.domain.Event.EventType.Football
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

    @PEnum EventType eventType = Football
    @PEnum EventSubType eventSubType = Training

    String creator
    String location
    String subject
    @Column(length = 2048) String description
    Integer maxNumber = Integer.MAX_VALUE

    @OneToManySubselect Set<Participant> participants = []
    @OneToManySubselect Set<History> history = []

    @Transient
    @JsonSerialize
    Long timeToRegistration() {
        regStart && DateTime.now().isBefore(regStart) ? regStart.getMillis()-DateTime.now().getMillis() : null
    }


    Event fetchMainCollections() {
        participants.size()
        history.size()
        this
    }

    Event updateParticipants(Participant participant = null) {
        if (hasManualLineUp()) {
            participant?.reserve = true
        } else {
            participants.eachWithIndex { Participant entry, int index ->
                entry.reserve = index >= maxNumber
            }
        }
        this
    }

    boolean hasManualLineUp() {
        eventSubType && eventSubType == Match
    }

    String asICalendarEvent() {
        DateTimeFormatter dt = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss")
        DateTimeZone tz = DateTimeZone.forID("Europe/Oslo")
        return """\
               BEGIN:VCALENDAR
               VERSION:2.0
               PRODID:-//BEKK//BEKK Fotball//NO
               BEGIN:VEVENT
               UID:$id@rsvp-app
               DTSTAMP;TZID=Europe/Oslo:${dt.print(DateTime.now().withZone(tz))}
               DTSTART;TZID=Europe/Oslo:${dt.print(startTime.withZone(tz))}
               DTEND;TZID=Europe/Oslo:${dt.print(endTime.withZone(tz))}
               SUMMARY:$subject
               LOCATION:$location
               END:VEVENT
               END:VCALENDAR""".stripIndent()
    }

    public static enum EventType {
        Football
    }

    public static enum EventSubType {
        Training,
        Match
    }

}
