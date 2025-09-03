package com.amit.SmartcontactManager.config;

import com.amit.SmartcontactManager.entities.User;
import com.amit.SmartcontactManager.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // fetching user from database
        User user = userRepository.getUserByUserName(username);

        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return customUserDetails;
    }
}
