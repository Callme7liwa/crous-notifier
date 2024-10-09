
package isima.crousnotifier.zzz.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isima.crousnotifier.zzz.models.User;
import isima.crousnotifier.zzz.service.UserService;

@RestController("/api/v1/notifier")
public class NotifierController {

    private final UserService userService;

    public NotifierController(UserService userService) {
        this.userService = userService;
    }
    
    

    @RequestMapping("/register")
    public User register(@RequestBody User user){
        User newUser = userService.registerUser(user);
        return newUser;
    }

}