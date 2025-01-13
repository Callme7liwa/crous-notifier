package isima.crousnotifier.zzz.services;

import isima.crousnotifier.zzz.repositories.LogementRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LogementService {

    @Value("${scripts.path}")
    private String scriptsPath;
    private final LogementRepository logementRepository;

    public LogementService(LogementRepository logementRepository) {
        this.logementRepository = logementRepository;
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void deleteAllLogement() {
        logementRepository.deleteAll();
    }

    @Scheduled(cron = "0 */2 * * * ?")
    public void extractLogements() {
        try {
            String projectPath = System.getProperty("user.dir");
            String[] command = {"python", projectPath + scriptsPath};
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
