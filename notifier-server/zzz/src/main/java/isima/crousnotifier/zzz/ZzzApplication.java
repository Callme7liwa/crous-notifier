package isima.crousnotifier.zzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import isima.crousnotifier.zzz.models.CrousNotifierBot;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZzzApplication {

    TelegramBotsApi telegramBotsApi;
    CrousNotifierBot crousNotifierBot;

    public ZzzApplication(TelegramBotsApi telegramBotsApi, CrousNotifierBot crousNotifierBot) {
        this.telegramBotsApi = telegramBotsApi;
        this.crousNotifierBot = crousNotifierBot;
    }

    public static void main(String[] args) {
        SpringApplication.run(ZzzApplication.class, args);
    }

}
