package com.stackroute.controller;

import com.stackroute.model.ConfirmationToken;
import com.stackroute.model.DAOUser;
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
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> register(@RequestBody DAOUser DAOUser)
    {
        DAOUser existingDAOUser =userRepository.findByEmailIdIgnoreCase(DAOUser.getEmailId());
        Map map=new HashMap<>();
        if(existingDAOUser !=null)
        {
            map.put("type","error");
            map.put("message","DAOUser already exist with given emailId");
            return new ResponseEntity<Map>(map,HttpStatus.CONFLICT);
        }
        else
        {
            String encryptPwd=bCryptPasswordEncoder.encode(DAOUser.getPassword());
            DAOUser.setPassword(encryptPwd);
            DAOUser.setRole("DAOUser");
            userRepository.save(DAOUser);
            ConfirmationToken confirmationToken = new ConfirmationToken(DAOUser);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(DAOUser.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("dileepkm6@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8888/api/v1/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            map.put("type","DAOUser");
            map.put("message","DAOUser successfully registered");
        }

        return new ResponseEntity<Map>(map,HttpStatus.OK);
    }
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAcc(@RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            DAOUser DAOUser = userRepository.findByEmailIdIgnoreCase(token.getDAOUser().getEmailId());
            DAOUser.setEnabled(true);
            userRepository.save(DAOUser);
            return new ResponseEntity<String>("account verified",HttpStatus.OK);
        }

            return new ResponseEntity<String>("account not verified",HttpStatus.CONFLICT);

    }

    @RequestMapping(value="/users", method= RequestMethod.GET)
    public ResponseEntity<?> allUsers()
    {
        DAOUser user=userRepository.findByEmailId("dileepkm6@gmail.com");
        return new ResponseEntity<DAOUser>(user,HttpStatus.OK);
    }

    @GetMapping("/getMember")
    public ResponseEntity<?> getMember(@RequestParam("emailId") String emailId)
    {
        DAOUser user = userRepository.findByEmailId(emailId);
        if(user!=null)
        {
            return new ResponseEntity<DAOUser>(user,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

}
