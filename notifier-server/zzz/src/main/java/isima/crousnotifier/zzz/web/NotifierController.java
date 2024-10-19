
package isima.crousnotifier.zzz.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/notifier")
public class NotifierController {

    @RequestMapping("/register")
    public String register() {
        return "hello world";
    }

}