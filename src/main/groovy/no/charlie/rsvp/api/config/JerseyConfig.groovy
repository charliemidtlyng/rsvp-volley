package no.charlie.rsvp.api.config

import no.charlie.rsvp.api.AdminResource
import no.charlie.rsvp.api.EventResource
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component

import javax.ws.rs.ApplicationPath

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
@ApplicationPath("/api")
class JerseyConfig extends ResourceConfig {

    JerseyConfig() {
        register JsonFeature
        register EventResource
        register AdminResource
    }

}
