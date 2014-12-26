package no.charlie.rsvp.domain

import org.joda.time.DateTime

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class Event {
    long id
    DateTime start
    DateTime end
    DateTime regStart
    DateTime regEnd

    Participant creator
    String location
    String subject
    String description
    Integer limit = Integer.MAX_VALUE

    Collection<Participant> participants = []
    Collection<Participant> reserveList = []
    Collection<History> history = []

}
