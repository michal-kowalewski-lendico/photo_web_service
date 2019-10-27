package com.mike.service.impl;

import com.mike.exception.UserServiceException;
import com.mike.io.repository.UserRepository;
import com.mike.io.entity.UserEntity;
import com.mike.shared.Utils;
import com.mike.shared.dto.AddressDto;
import com.mike.shared.dto.UserDto;
import com.mike.service.UserService;
import com.mike.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDto> getUsers(int page, int limit) {

        // by default this method returns first page with index 0
        // but better user exp is to start with page 1
        if(page > 0) page--;

        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> entityPage = userRepository.findAll(pageable);
        List<UserEntity> userEntities = entityPage.getContent();

        List<UserDto> returnValue = new ArrayList<>();

        for(UserEntity userEntity : userEntities){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record already exist");

        ModelMapper modelMapper = new ModelMapper();

        for(int i=0; i < userDto.getAddresses().size(); i++){

             AddressDto addressDto = userDto.getAddresses().get(i);
             addressDto.setAddressId(utils.generateAddressId(30));
             addressDto.setUserDetails(userDto);
             userDto.getAddresses().set(i, addressDto);

        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserEntity = userRepository.save(userEntity);

        return modelMapper.map(storedUserEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

//        it could be UsernameNotFoundException
//        if(null == userEntity) throw new UsernameNotFoundException(email);
//        or
        if(null == userEntity) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(null == userEntity) throw new UsernameNotFoundException("User with Id: " + userId + " not found.");

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(null == userEntity) throw new UsernameNotFoundException(userId);

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUser = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(null == userEntity) throw new UsernameNotFoundException(userId);

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(null == userEntity) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
