package com.example.attendance.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.UserInfo;
import com.example.attendance.mapper.UserInfoMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserInfoMapper userInfoMapper;

    public CustomUserDetailsService(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserInfo userInfo = userInfoMapper.findByUserId(username);

        if (userInfo == null) {
            throw new UsernameNotFoundException("ユーザが存在しません");
        }

        return User.builder()
                .username(userInfo.getUserId())
                .password(userInfo.getUserPass())
                .roles(convertRole(userInfo.getUserAuthority()))
                .build();
    }

    private String convertRole(String authority) {

        if ("ROLE_ADMIN".equals(authority)) {
            return "ADMIN";
        }

        return "USER";
    }
}