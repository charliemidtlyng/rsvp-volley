package no.charlie.rsvp.service

import groovyx.net.http.HTTPBuilder
import no.charlie.rsvp.domain.Participant
import org.springframework.stereotype.Service

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */

@Service("smsService")
class SmsServiceImpl implements SmsService {
    public static final FROM_NUMBER = '99402316';

    @Override
    void sendSms(Participant participant, String message) {
        if (!participant.phoneNumber) {
            return;
        }
        String password = System.getenv('SMS_PASSWORD')

        def http = new HTTPBuilder('https://telenormobil.no')
        http.get(
                path: '/ums/smapi/input',
                query: [
                        sender             : FROM_NUMBER,
                        password           : password,
                        recipients         : participant.phoneNumber,
                        sId                : '1000000000',
                        content            : message,
                        responseContentType: 'text/plain'

                ]);


    }
}
