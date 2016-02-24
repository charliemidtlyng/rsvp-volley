package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Otp
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.repository.OtpRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Unroll
class OtpServiceImplTest extends Specification {

    OtpRepository otpRepository = Mock(OtpRepository)
    OtpService otpService = new OtpServiceImpl(
            otpRepository: otpRepository
    )

    def 'should fail if [#description] are created for event'() {
        when:
            otpRepository.findByEventId(_) >> result
            otpService.validateOtp(1L, '1234')
        then:
            Exception e = thrown()
            e instanceof RsvpBadRequestException
        where:
            result                                                                           | description
            []                                                                               | 'no otps'
            [new Otp(eventId: 1L, password: '1233')]                                         | 'otp with different password'
            [new Otp(eventId: 1L, password: '1233'), new Otp(eventId: 1L, password: '1235')] | 'multiple otps with different password'
    }

    def 'should not throw exception if matching an otp'() {
        when:
            otpRepository.findByEventId(_) >> result
            otpService.validateOtp(1L, '1234')
        then:
            notThrown(Exception)
        where:
            result                                                                           | description
            [new Otp(eventId: 1L, password: '1234')]                                         | 'one otp'
            [new Otp(eventId: 1L, password: '1233'), new Otp(eventId: 1L, password: '1234')] | 'multiple otps'
    }
}
