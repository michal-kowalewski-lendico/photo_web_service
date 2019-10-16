package com.mike.service.impl;

import com.mike.UserRepository;
import com.mike.io.entity.UserEntity;
import com.mike.shared.Utils;
import com.mike.shared.dto.UserDto;
import com.mike.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Override
    public UserDto createUser(UserDto userDto) {

        if(userRepository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record already exist");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword("testPassword");

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnValue);

        return returnValue;
    }
}
