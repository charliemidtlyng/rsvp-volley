package no.charlie.rsvp.api.config

import com.fasterxml.jackson.databind.AnnotationIntrospector
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector

import javax.ws.rs.core.Feature
import javax.ws.rs.core.FeatureContext

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class JsonFeature implements Feature {

    @Override
    boolean configure(FeatureContext context) {
        context.register(createJacksonJsonProvider())
        true
    }

    private static JacksonJsonProvider createJacksonJsonProvider() {
        JacksonJsonProvider provider = new JacksonJaxbJsonProvider()
        ObjectMapper mapper = new ObjectMapper()
        mapper.registerModule(new JodaModule())
        mapper.registerModule(new RsvpJodaModule())
//        mapper.registerModule(new Hibernate4Module())
        AnnotationIntrospector primary = new JacksonAnnotationIntrospector()
        AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory())
        AnnotationIntrospector introspector = new AnnotationIntrospectorPair(primary, secondary)
        mapper.setAnnotationIntrospector(introspector)

        provider.setMapper(mapper)
        return provider;
    }
}
