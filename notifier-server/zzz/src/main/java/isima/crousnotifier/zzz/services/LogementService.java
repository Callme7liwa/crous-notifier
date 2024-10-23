package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.repositories.LogementRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LogementService {
    private final LogementRepository logementRepository;

    public LogementService(LogementRepository logementRepository) {
        this.logementRepository = logementRepository;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void deleteAllLogement() {
        logementRepository.deleteAll();
    }

    @Scheduled(cron = "0 */3 * * * ?")
    public void extractLogements() {
        try {
            String[] command = {"python", "C:\\Users\\Kabouri\\Desktop\\ISIMA ZZ3\\Projet\\crous-notifier\\extraction-server\\src\\scripts\\automate_search.py"};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            process.waitFor();
            System.out.println("Script Python exécuté avec succès.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'exécution du script Python.");
        }
    }
}
