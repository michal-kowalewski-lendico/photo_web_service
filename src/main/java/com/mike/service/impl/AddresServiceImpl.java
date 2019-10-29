package com.mike.service.impl;

import com.mike.io.entity.AddressEntity;
import com.mike.io.entity.UserEntity;
import com.mike.io.repository.AddressRepository;
import com.mike.io.repository.UserRepository;
import com.mike.service.AddressService;
import com.mike.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddresServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<AddressDto> getAddressesByUserId(String userId) {

        List<AddressDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) return returnValue;

        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(userEntity);
        for(AddressEntity addressEntity : addressEntities){
            returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddressById(String addressId) {

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if (addressEntity == null) return null;

        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
