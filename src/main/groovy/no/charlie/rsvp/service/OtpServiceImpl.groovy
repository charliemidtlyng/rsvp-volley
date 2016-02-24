package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Otp
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.repository.OtpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static no.charlie.rsvp.domain.Otp.generatePassword

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Service("otpService")
class OtpServiceImpl implements OtpService {

    @Autowired OtpRepository otpRepository

    Otp resolveOtpForEventId(Long eventId) {
        otpRepository.save(createOtp(eventId))
    }

    void validateOtp(long eventId, String password) {
        List<Otp> otps = otpRepository.findByEventId(eventId)
        Otp otp = otps.find { it.password == password }
        if (!otp) {
            throw new RsvpBadRequestException("Engangspassord validerte ikke. ")
        } else {
            otpRepository.delete(otp.id)
        }

    }

    private static Otp createOtp(Long eventId) {
        new Otp(eventId: eventId, password: generatePassword())
    }
}
