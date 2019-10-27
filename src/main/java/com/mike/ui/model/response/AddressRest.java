package com.mike.ui.model.response;

import lombok.Data;

@Data
public class AddressRest {

    private String addressId;
    private String country;
    private String city;
    private String streetName;
    private String postalCode;
    private String type;

}
