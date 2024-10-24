package isima.crousnotifier.zzz.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import isima.crousnotifier.zzz.configs.TwilioConfiguration;
import isima.crousnotifier.zzz.models.SmsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender {

    private final TwilioConfiguration twilioConfiguration;
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);

    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    public void sendSms(SmsRequest smsRequest) {
        if (isPhoneNumberValid(smsRequest.getPhoneNumber())) {
            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
            String message = smsRequest.getMessage();
            Message creator = Message.creator(to, from, message).create();
            LOGGER.info("Send sms : " + smsRequest);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [ " + smsRequest.getPhoneNumber() + " ] is not valid number"
            );
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // TODO: Implement phone number validator
        return true;
    }
}
