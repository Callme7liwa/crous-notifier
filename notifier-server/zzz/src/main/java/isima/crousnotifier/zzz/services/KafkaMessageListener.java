package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.models.Logement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(topics = "housing_topic", groupId = "housing_consumer_group")
    public void consumeEvents(Logement logement) {
        System.out.println("consumer consume the events ");
        log.info("consumer consume the events {} " + logement.toString());



//        emailSenderService.sendEmail(
//                user.getEmail(),
//                "Nous avons trouvé des logements pour vous ! À ne pas manquer",
//                "Bonjour " + user.getFullName() + ",\n\n" +
//                        "Nous avons de bonnes nouvelles pour vous ! De nouvelles propositions de logements qui pourraient correspondre à vos critères sont disponibles dès maintenant.\n\n" +
//                        "Nous vous invitons à les découvrir sur la plateforme messervices en suivant ce lien :\n" +
//                        "➡️ https://trouverunlogement.lescrous.fr/\n\n" +
//                        "N'attendez pas trop longtemps, les offres partent rapidement !\n\n" +
//                        "Cordialement,\n" +
//                        "L’équipe Crous Notifier\n" +
//                        "Simplifiez votre recherche de logement étudiant."
//        );


    }
}