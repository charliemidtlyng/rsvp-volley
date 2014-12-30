package no.charlie.rsvp.api.config

import org.springframework.context.annotation.Configuration

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
