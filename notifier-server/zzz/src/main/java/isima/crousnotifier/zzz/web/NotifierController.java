
package isima.crousnotifier.zzz.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import isima.crousnotifier.zzz.service.TelegramService;

@RestController()
@RequestMapping("/notifier")
public class NotifierController {
    private final TelegramService telegramService;

    public NotifierController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/send-to-group")
    public String sendMessageToGroup(@RequestParam String message) {
        telegramService.sendMessageToGroup(message);
        return "Message sent to group!";
    }
}