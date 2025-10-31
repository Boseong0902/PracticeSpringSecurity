package SprSecu.demo.service;

import SprSecu.demo.dto.CustomUserDetails;
import SprSecu.demo.entity.UserEntity;
import SprSecu.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserEntity userData = userRepository.findByUsername(username);
        if(userData == null){
            return null;
        }
        else {
            return new CustomUserDetails(userData);
        }
    }
}
