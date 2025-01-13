package isima.crousnotifier.zzz.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession; // Correct import

import isima.crousnotifier.zzz.models.CrousNotifierBot;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(CrousNotifierBot crousNotifierBot)
            throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(crousNotifierBot);
        return botsApi;
    }

    @Bean
    public CrousNotifierBot crousNotifierBot() {
        return new CrousNotifierBot();
    }
}
