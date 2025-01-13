package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.models.Logement;
import isima.crousnotifier.zzz.models.SmsRequest;
import isima.crousnotifier.zzz.models.User;
import isima.crousnotifier.zzz.repositories.LogementRepository;
import isima.crousnotifier.zzz.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LogementRepository logementRepository;
    private final EmailSenderService emailSenderService;
    private final TwilioSmsSender twilioSmsSender;

    public Optional<User> registerUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new Exception("Email already exists.");
        }

        emailSenderService.sendEmail(
                user.getEmail(),
                "Bienvenue sur Crous Notifier ! Votre recherche de logement commence ici",
                "Bonjour " + user.getFullName() + ",\n\n" +
                        "Merci de vous être inscrit sur Crous Notifier ! 🎉 Nous sommes heureux de vous accompagner dans votre recherche de logement.\n\n" +
                        "À partir de maintenant, vous recevrez régulièrement par e-mail/SMS des propositions de logements adaptés à vos besoins. Soyez attentif à nos notifications \uD83D\uDD14.\n\n" +
                        "Nous mettons tout en œuvre pour vous aider à trouver rapidement le logement qui vous convient. Vous pourrez postuler directement aux offres qui vous intéressent via les liens que vous recevrez dans nos e-mails.\n\n" +
                        "L’équipe Crous Notifier\n" +
                        "Simplifiez votre recherche de logement étudiant."
        );

        SmsRequest smsRequest = new SmsRequest(user.getPhoneNumber(), user.getFullName() + ", Votre compte a été crée avec succès.");
        twilioSmsSender.sendSms(smsRequest);

        User savedUser = userRepository.save(user);

        Thread emailThread = new Thread(() -> {
            try {
                Thread.sleep(20000);
                List<String> codesZips = List.of(user.getListCodeZip().split(", "));
                System.out.println("---------------- codesDepartements ---------------");
                for (String code : codesZips) {
                    System.out.println(code);
                }
                System.out.println("---------------- end ---------------");
                List<Logement> logements = null;
                for(String code: codesZips) {
                    logements = logementRepository.findByCodeZip(code);
                }
                if(!logements.isEmpty()) {
                    StringBuilder emailContent = new StringBuilder("Bonjour " + user.getFullName() + ",\n\n");
                    emailContent.append("Voici quelques logements correspondant à vos critères :\n\n");
                    for (Logement logement : logements) {
                        emailContent.append("- ").append(logement.getTitre()).append(", ")
                                .append(logement.getDescription()).append("\n");
                    }
                    emailContent.append("\nL’équipe Crous Notifier");
                    emailSenderService.sendEmail(user.getEmail(), "Nouvelles propositions de logement", String.valueOf(emailContent));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        emailThread.start();
        try {
            emailThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de l'exécution du thread pour les emails");
        }
        return Optional.of(savedUser);
    }
}