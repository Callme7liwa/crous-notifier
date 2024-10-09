package isima.crousnotifier.zzz.service;

import org.springframework.stereotype.Service;

import isima.crousnotifier.zzz.models.User;
import isima.crousnotifier.zzz.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User registerUser(User user) {

        User newUser = new User();
        
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setListCrous(user.getListCrous());

        
        
        return newUser;
    }
    
}
