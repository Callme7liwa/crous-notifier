package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.models.Logement;
import isima.crousnotifier.zzz.models.SmsRequest;
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
    private final TwilioSmsSender twilioSmsSender;
    private final TelegramService telegramService;
    private Map<User, List<Logement>> userLogementsMap = new ConcurrentHashMap<>();
    private long delayBeforeSendingNotification = 15;
    private Timer timer = new Timer();
    private Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    public KafkaMessageListener(UserRepository userRepository,
                                LogementRepository logementRepository,
                                EmailSenderService emailSenderService,
                                TwilioSmsSender twilioSmsSender,
                                TelegramService telegramService) {
        this.userRepository = userRepository;
        this.logementRepository = logementRepository;
        this.emailSenderService = emailSenderService;
        this.twilioSmsSender = twilioSmsSender;
        this.telegramService = telegramService;
    }

    @KafkaListener(topics = "housing_topic", groupId = "housing_consumer_group")
    public void consumeEvents(Logement logement) {
        Logement existingLogement = logementRepository.findByTitre(logement.getCodeZip());
        if (existingLogement != null) {
            log.info("Logement déjà présent dans la base de données.");
        } else {
            logementRepository.save(logement);
            List<User> users = userRepository.findByListCodeZip(logement.getCodeZip());
            for (User user : users) {
                userLogementsMap.computeIfAbsent(user, k -> new ArrayList<>()).add(logement);
            }
        }
        scheduleNotificationTask();
    }

    private void scheduleNotificationTask() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendNotification();
            }
        }, delayBeforeSendingNotification * 1000);
    }

    private void sendNotification() {
        for (Map.Entry<User, List<Logement>> entry : userLogementsMap.entrySet()) {
            User user = entry.getKey();
            List<Logement> logements = entry.getValue();
            if (!logements.isEmpty()) {
                String emailContent = generateEmailContent(logements);
                emailSenderService.sendEmail(user.getEmail(), "Nouvelles propositions de logement", emailContent);
                SmsRequest smsRequest = new SmsRequest(user.getPhoneNumber(), generateSmsContent());
                twilioSmsSender.sendSms(smsRequest);
            }
        }
        userLogementsMap.clear();
    }

    private String generateEmailContent(List<Logement> logements) {
        StringBuilder content = new StringBuilder("Voici les nouvelles propositions de logement disponibles:\n\n");
        content.append(generateListLogements(logements));
        content.append("Nous vous invitons à les découvrir sur la plateforme messervices en suivant ce lien :").append("\n\n")
                .append("➡️ https://trouverunlogement.lescrous.fr/").append("\n\n")
                .append("N'attendez pas trop longtemps, les offres partent rapidement !").append("\n\n")
                .append("Cordialement,").append("\n")
                .append("L’équipe Crous Notifier").append("\n")
                .append("Simplifiez votre recherche de logement étudiant.");
        return content.toString();
    }

    private String generateListLogements(List<Logement> logements) {
        StringBuilder content = new StringBuilder();
        for (Logement logement : logements) {
            content.append("- ").append(logement.getTitre()).append(", ")
                    .append(logement.getDescription()).append("\n\n");
        }
        telegramService.sendMessageToGroup("Nouvelles Propositions 🎉:\n" + content.toString());
        return content.toString();
    }

    private String generateSmsContent() {
        StringBuilder content = new StringBuilder("Nouvelles propositions de logement\n");
        content.append("Nous vous invitons à les découvrir sur https://trouverunlogement.lescrous.fr/").append("\n")
                .append("N'attendez pas trop longtemps, les offres partent rapidement !").append("\n")
                .append("Cordialement,").append("\n")
                .append("L’équipe Crous Notifier").append("\n");
        return content.toString();
    }
}