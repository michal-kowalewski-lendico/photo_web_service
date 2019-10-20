package com.mike.ui.controller;

import com.mike.shared.dto.UserDto;
import com.mike.ui.model.request.UserDetailsRequestModel;
import com.mike.ui.model.response.UserRest;
import com.mike.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}",
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){

        UserDto userDto = userService.getUserByUserId(id);
        UserRest returnValue = new UserRest();

        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel){

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = new UserRest();

        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser(){
        return "update was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "delete was called";
    }
}
