package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.models.Logement;
import isima.crousnotifier.zzz.models.User;
import isima.crousnotifier.zzz.repositories.LogementRepository;
import isima.crousnotifier.zzz.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class KafkaMessageListener {
    private final LogementRepository logementRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private Map<User, List<Logement>> userLogementsMap = new ConcurrentHashMap<>();
    private long delayBeforeSendingEmails = 15;
    private Timer timer = new Timer();
    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    public KafkaMessageListener(UserRepository userRepository,
                                LogementRepository logementRepository,
                                EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.logementRepository = logementRepository;
        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "housing_topic", groupId = "housing_consumer_group")
    public void consumeEvents(Logement logement) {
        Optional<Logement> existingLogement = logementRepository.findByCodeZip(logement.getCodeZip());
        if (existingLogement.isPresent()) {
            log.info("Logement déjà présent dans la base de données!");
        } else {
            logementRepository.save(logement);
            String codeZip = logement.getCodeZip();
            String codeDepartement = codeZip.substring(0, 2);
            List<User> users = userRepository.findByListCodeDepartement(codeDepartement);
            for (User user : users) {
                userLogementsMap.computeIfAbsent(user, k -> new ArrayList<>()).add(logement);
            }
        }
        scheduleEmailTask();
    }

    private void scheduleEmailTask() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendEmails();
            }
        }, delayBeforeSendingEmails * 1000);
    }

    private void sendEmails() {
        for (Map.Entry<User, List<Logement>> entry : userLogementsMap.entrySet()) {
            User user = entry.getKey();
            System.out.println("-----------------------------------> User : "+user.toString());
            List<Logement> logements = entry.getValue();
            if (!logements.isEmpty()) {
                String emailContent = generateEmailContent(logements);
                emailSenderService.sendEmail(user.getEmail(), "Nouvelles propositions de logement", emailContent);
            }
        }
        userLogementsMap.clear();
    }

    private String generateEmailContent(List<Logement> logements) {
        StringBuilder content = new StringBuilder("Voici les nouvelles propositions de logement disponibles:\n\n");
        for (Logement logement : logements) {
            content.append("- ").append(logement.getTitre()).append(", ")
                    .append(logement.getDescription()).append("\n\n");
        }
        content.append("Nous vous invitons à les découvrir sur la plateforme messervices en suivant ce lien :").append("\n\n")
                .append("➡️ https://trouverunlogement.lescrous.fr/").append("\n\n")
                .append("N'attendez pas trop longtemps, les offres partent rapidement !").append("\n\n")
                .append("Cordialement,").append("\n")
                .append("L’équipe Crous Notifier").append("\n")
                .append("Simplifiez votre recherche de logement étudiant.");
        return content.toString();
    }


}