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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class UserAccountController {
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user)
    {
        User existingUser=userRepository.findByEmailIdIgnoreCase(user.getEmailId());
        Map map=new HashMap<>();
        if(existingUser!=null)
        {
            map.put("type","error");
            map.put("message","user already exist with given emailId");
            return new ResponseEntity<Map>(map,HttpStatus.CONFLICT);
        }
        else
        {
            String encryptPwd=bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encryptPwd);
            user.setRole("user");
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

            map.put("type","user");
            map.put("message","user successfully registered");
        }

        return new ResponseEntity<Map>(map,HttpStatus.OK);
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

//    @RequestMapping(value="/signIn", method= RequestMethod.GET)
//    public ResponseEntity<?> signIn(@RequestP)


}
