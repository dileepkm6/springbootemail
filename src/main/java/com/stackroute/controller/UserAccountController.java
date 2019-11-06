package com.stackroute.controller;

import com.stackroute.model.ConfirmationToken;
import com.stackroute.model.User;
import com.stackroute.repository.ConfirmationTokenRepository;
import com.stackroute.repository.UserRepository;
import com.stackroute.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserAccountController {
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user)
    {
        User existingUser=userRepository.findByEmailIdIgnoreCase(user.getEmailId());
        if(existingUser!=null)
        {
            return new ResponseEntity<String>("user already exist",HttpStatus.CONFLICT);
        }
        else
        {
            userRepository.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("dileepkm6@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8888/api/v1/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);
        }

        return new ResponseEntity<>("message successfully send",HttpStatus.OK);
    }
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAcc(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
            user.setEnabled(true);
            userRepository.save(user);
            return new ResponseEntity<String>("account verified",HttpStatus.OK);
        }

            return new ResponseEntity<String>("account not verified",HttpStatus.CONFLICT);

    }


}
