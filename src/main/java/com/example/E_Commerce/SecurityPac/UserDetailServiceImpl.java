package com.example.E_Commerce.SecurityPac;


import com.example.E_Commerce.Entities.UserClass;
import com.example.E_Commerce.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClass userClass = userRepo.findByUserName(username);
        if (userClass ==null){
            throw new UsernameNotFoundException("UsernameNotFoundException!!!");
        }
        return new UserDetailsImpl(userClass);
    }
}
