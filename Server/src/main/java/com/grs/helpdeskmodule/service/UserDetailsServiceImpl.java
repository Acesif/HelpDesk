package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.dto.UserPrincipal;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUserByEmail = userRepository.findByEmail(username);
        if (findUserByEmail == null){
            throw new UsernameNotFoundException("user does not exist");
        }
        return new UserPrincipal(findUserByEmail);
    }
}
