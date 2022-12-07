package com.wangkisa.commerce.security;

import com.wangkisa.commerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new SecurityUser(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found by email : " + email)));
    }
}
