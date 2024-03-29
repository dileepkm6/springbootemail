package com.stackroute.controller;

import com.stackroute.model.Company;
import com.stackroute.model.CompanyDetails;
import com.stackroute.model.ConfirmationToken;
import com.stackroute.model.DAOUser;
import com.stackroute.repository.CompanyRepository;
import com.stackroute.repository.ConfirmationTokenRepository;
import com.stackroute.repository.UserRepository;
import com.stackroute.services.CompanyRegistrationService;
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
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/api/v1/company/")
public class CompanyController {
    @Autowired
    CompanyRegistrationService companyRegistrationService;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    BCryptPasswordEncoder cryptPasswordEncoder;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    UserRepository userRepository;
    @PostMapping("registerCompany")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyDetails companyDetails)
    {
        Company existCompany=companyRepository.findByCompanyIdIgnoreCase(companyDetails.getCompanyId());
        DAOUser existDAOUser =userRepository.findByEmailIdIgnoreCase(companyDetails.getEmailId());
        Map map=new HashMap<>();
        if(existCompany!=null)
        {
            map.put("type","error");
            map.put("message","company already exist with given Id");
            return new ResponseEntity<Map>(map,HttpStatus.CONFLICT);
        }
        else if(existDAOUser !=null)
        {
            map.put("type","error");
            map.put("message","user already exist with given emailId");
            return new ResponseEntity<Map>(map,HttpStatus.CONFLICT);
        }
        else
        {
            String encodedPass=cryptPasswordEncoder.encode(companyDetails.getPassword());
            companyDetails.setPassword(encodedPass);
            DAOUser DAOUser =new DAOUser();
            DAOUser.setEmailId(companyDetails.getEmailId());
            DAOUser.setPassword(companyDetails.getPassword());
            DAOUser.setRole("admin");
            userRepository.save(DAOUser);
            Company company=new Company(companyDetails.getCompanyId(),companyDetails.getCompanyName(), DAOUser);
            companyRegistrationService.saveCompany(company);

            ConfirmationToken confirmationToken=new ConfirmationToken(DAOUser);
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(DAOUser.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("dileepkm6@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8888/api/v1/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            map.put("type","admin");
            map.put("message","company successfully registered");
        }
        return new ResponseEntity<Map>(map,HttpStatus.OK);
    }
}
