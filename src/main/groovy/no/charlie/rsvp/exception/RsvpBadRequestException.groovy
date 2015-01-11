package no.charlie.rsvp.exception

import javax.ws.rs.BadRequestException
import javax.ws.rs.core.Response

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class RsvpBadRequestException extends BadRequestException {
    public RsvpBadRequestException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(message)
                .build());
    }

}
