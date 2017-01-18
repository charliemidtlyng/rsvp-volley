package no.charlie.rsvp.utils

import no.charlie.rsvp.exception.RsvpBadRequestException

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class ValidatorUtils {
    static Boolean validateProperties(Map map, String... properties) {
        if (!map) {
            throw new RsvpBadRequestException("Mangelfull utfylling")
        }
        properties.each {
            if (!map.containsKey(it)) {
                throw new RsvpBadRequestException("Feltet $it mangler!")
            }
        }
    }

    static Boolean validatePhoneNumber(Map map) {
        String phoneNumber = map.phoneNumber
        if (phoneNumber) {
            if (phoneNumber.length() == 8 && (phoneNumber.startsWith('4') || phoneNumber.startsWith('9'))) {
                return true
            }
            throw new RsvpBadRequestException("Feltet mobiltlf må være 8 tegn og begynne på 9 eller 4!")
        }
        return true
    }

}
