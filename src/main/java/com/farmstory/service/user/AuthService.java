package com.farmstory.service.user;

import com.farmstory.config.MyUserDetails;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepository.findByUserId(username);

        if(optUser.isPresent()) {
            // 시큐리티 사용자 인증객체 생성 후 반환
            return MyUserDetails.builder()
                    .user(optUser.get())
                    .build();
        }


        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
