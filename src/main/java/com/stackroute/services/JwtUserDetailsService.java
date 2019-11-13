package com.stackroute.services;

import com.stackroute.model.DAOUser;
import com.stackroute.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DAOUser user=userRepository.findByEmailId(username);
        System.out.println(username);
        if (user!=null) {
            return new User(user.getEmailId(), user.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("DAOUser not found with username: " + username);
        }
    }
}