package isima.crousnotifier.zzz.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import isima.crousnotifier.zzz.models.CrousNotifierBot;  // Corrected package

@Service
@AllArgsConstructor
public class TelegramService {
    private CrousNotifierBot crousNotifierBot;

    public void sendMessageToGroup(String message) {
        crousNotifierBot.sendMessageToGroup(message);
    }
    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            crousNotifierBot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
