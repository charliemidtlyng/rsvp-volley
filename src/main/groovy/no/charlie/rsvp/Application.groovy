package no.charlie.rsvp

import org.slf4j.Logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
import org.springframework.context.annotation.ComponentScan

import static org.slf4j.LoggerFactory.getLogger

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@EnableAutoConfiguration(exclude = GroovyTemplateAutoConfiguration)
@ComponentScan
class Application {

    private static final Logger LOGGER = getLogger(Application)

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application, args)
    }
}
