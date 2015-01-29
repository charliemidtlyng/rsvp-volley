package no.charlie.rsvp.api

import no.charlie.rsvp.service.EventService
import no.charlie.rsvp.service.ReCaptchaService
import spock.lang.Specification

import javax.ws.rs.core.Response

import static javax.ws.rs.core.Response.Status.ACCEPTED

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class EventResourceTest extends Specification {

    def eventService = Mock(EventService)
    def recaptchaService = Mock(ReCaptchaService)
    EventResource resource

    def setup() {
        resource = new EventResource(
                eventService: eventService,
                captchaService: recaptchaService
        )
    }


    def "should fail when missing properties"() {
        when:
            Map valueMap = [:]
            resource.createEvent(valueMap)
        then:
            Exception e = thrown()
    }

    def "should create event"() {
        when:
            Map valueMap = [
                    "subject"    : "Trening",
                    "startTime"  : "2015-01-01T20:00:00",
                    "endTime"    : "2015-01-01T21:00:00Z",
                    "regStart"   : "2014-12-29T14:03:00Z",
                    "regEnd"     : "2015-01-01T20:00:00Z",
                    "creator"    : "Charlie",
                    "location"   : "Vallhall",
                    "description": "Vanlig trening",
                    "maxNumber"  : 15
            ]
            Response response = resource.createEvent(valueMap)
        then:
            response.status == ACCEPTED.statusCode
    }

    def "should add participant to event"() {
        when:
            Map valueMap = [
                    "name" : "Navn navnesen",
                    "email": "navn@navnesen.no"
            ]
            Response response = resource.register(1L, valueMap)
        then:
            recaptchaService.isHuman(_, _) >> true
            response.status == ACCEPTED.statusCode

    }

    def "should fail when participant is missing properties"() {
        when:
            Map valueMap = [:]
            resource.register(1L, valueMap)
        then:
            Exception e = thrown()
    }

    def "should remove participant from event"() {
        when:
            Response response = resource.unregister(1L, 1L)
        then:
            response.status == ACCEPTED.statusCode

    }
}
