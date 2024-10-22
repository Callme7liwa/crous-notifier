package isima.crousnotifier.zzz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import isima.crousnotifier.zzz.models.CrousNotifierBot;  // Corrected package

@Service
public class TelegramService {

    @Autowired
    private CrousNotifierBot crousNotifierBot;

    // Send a message to a specific chat ID or group
    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            crousNotifierBot.execute(message);  // Executes the message
        } catch (TelegramApiException e) {
            e.printStackTrace();  // Logs error in case of failure
        }
    }

    // Optionally, method to send directly to a predefined group
    public void sendMessageToGroup(String message) {
        crousNotifierBot.sendMessageToGroup(message);
    }
}
