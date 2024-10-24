package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.models.SmsRequest;
import isima.crousnotifier.zzz.models.User;
import isima.crousnotifier.zzz.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final TwilioSmsSender twilioSmsSender;

    public User registerUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new Exception("Email already exists");
        }
        emailSenderService.sendEmail(
                user.getEmail(),
                "Bienvenue sur Crous Notifier ! Votre recherche de logement commence ici",
                "Bonjour " + user.getFullName() + ",\n\n" +
                        "Merci de vous être inscrit sur Crous Notifier ! 🎉 Nous sommes heureux de vous compter parmi nous et de vous accompagner dans votre recherche de logement étudiant.\n\n" +
                        "À partir de maintenant, vous recevrez régulièrement par e-mail/SMS des propositions de logements adaptés à vos besoins. Soyez attentif à nos notifications \uD83D\uDD14.\n\n" +
                        "Nous mettons tout en œuvre pour vous aider à trouver rapidement le logement qui vous convient. Vous pourrez postuler directement aux offres qui vous intéressent via les liens que vous recevrez dans nos e-mails.\n\n" +
                        "Bienvenue et bonne chance dans votre recherche de logement !\n\n" +
                        "L’équipe Crous Notifier\n" +
                        "Simplifiez votre recherche de logement étudiant."
        );
        SmsRequest smsRequest = new SmsRequest(user.getPhoneNumber(), user.getFullName() + ", Votre compte a été crée avec succès !");
        twilioSmsSender.sendSms(smsRequest);
        return userRepository.save(user);
    }
}