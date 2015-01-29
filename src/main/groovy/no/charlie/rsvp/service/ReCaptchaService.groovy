package no.charlie.rsvp.service

import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.springframework.stereotype.Service

import javax.ws.rs.core.UriBuilder

import static org.slf4j.LoggerFactory.getLogger

/**
 * @author Torstein Nicolaysen (torstein.nicolaysen@BEKK.no)
 */
@Service("reCaptchaService")
class ReCaptchaService implements CaptchaService {

    static final String Secret = System.getenv('GOOGLE_RECAPTCHA_SECRET_KEY');
    static final String BaseUri = "https://www.google.com/recaptcha/api/siteverify";

    private final Logger LOGGER = getLogger(ReCaptchaService)

    @Override
    boolean isHuman(String input, String ip) {
        if (!input) {
            return false
        }

        URI requestUrl = UriBuilder.fromPath(BaseUri)
                .queryParam("secret", Secret)
                .queryParam("response", input)
                .queryParam("remoteip", ip)
                .build();

        try {
            HttpURLConnection connection = (HttpURLConnection) requestUrl.toURL().openConnection();
            int responseCode = connection.getResponseCode();
            return responseCode == 200 ? isResponseValid(connection.getInputStream()) : false
        } catch (RuntimeException e) {
            this.LOGGER.warn("ReCaptcha validation failed", e);
            return false;
        }
    }

    /**
     * Error code reference
     * missing-input-secret	    The secret parameter is missing.
     * invalid-input-secret	    The secret parameter is invalid or malformed.
     * missing-input-response	The response parameter is missing.
     * invalid-input-response	The response parameter is invalid or malformed
     */
    boolean isResponseValid(InputStream stream) {
        def response = new JsonSlurper().parse(stream);

        if (!response.success) {
            this.LOGGER.warn("Captcha validation failed. Error(s) from Google: " + response['error-codes'].join(","))
            return false
        }

        return true;
    }
}