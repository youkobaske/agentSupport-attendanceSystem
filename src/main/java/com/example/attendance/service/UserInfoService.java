package com.example.attendance.service;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.UserInfo;
import com.example.attendance.mapper.UserInfoMapper;

@Service
public class UserInfoService {
	private final UserInfoMapper userInfoMapper;

	public UserInfoService(UserInfoMapper userInfoMapper) {
		this.userInfoMapper = userInfoMapper;
	}

	public UserInfo getUserState(String userId) {
		return userInfoMapper.findByUserId(userId);
	}
}
