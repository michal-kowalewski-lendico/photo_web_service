package com.mike.service;

import com.mike.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserId(String userId);
}
