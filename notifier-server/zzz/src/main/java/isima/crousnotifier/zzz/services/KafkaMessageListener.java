package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.dtos.Logement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(topics = "housing-topic",groupId = "housing_consumer_group")
    public void consumeEvents(Logement logement) {
        log.info("consumer consume the events {} ", logement.toString());
    }
}
