package no.charlie.rsvp.api.config

import no.charlie.rsvp.api.EventResource
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
class JerseyConfig extends ResourceConfig {

    JerseyConfig() {
        register JsonFeature
        register EventResource
    }

}
