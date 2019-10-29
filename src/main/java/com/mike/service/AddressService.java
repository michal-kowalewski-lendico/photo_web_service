package com.mike.service;

import com.mike.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddressesByUserId(String userId);

    AddressDto getAddressById(String addressId);
}
