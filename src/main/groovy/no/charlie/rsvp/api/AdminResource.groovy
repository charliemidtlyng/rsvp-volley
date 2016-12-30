package no.charlie.rsvp.api

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.service.EventService
import no.charlie.rsvp.service.MailService
import no.charlie.rsvp.service.SlackService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/admin")
class AdminResource {

    @Autowired EventService eventService
    @Autowired MailService mailService
    @Autowired SlackService slackService

    @GET
    @Path('/events/{eventId}/sendNotification/slack')
    Response sendSlackNotification(@PathParam('eventId') Long eventId) {

        Event event = findAndValidateEvent(eventId)
        slackService.sendEventNotification(event)
        return Response.ok(event).build()
    }

    @GET
    @Path('/events/{eventId}/sendNotification/mail')
    Response sendMailNotification(@PathParam('eventId') Long eventId) {

        Event event = findAndValidateEvent(eventId)
        mailService.sendEventNotification(event)
        return Response.ok(event).build()
    }

    private Event findAndValidateEvent(long eventId) {
        Event event = eventService.findEventById(eventId)
        if (event == null) {
            throw new RsvpBadRequestException("Can not find event by id ${eventId}")
        }
        event
    }
}
