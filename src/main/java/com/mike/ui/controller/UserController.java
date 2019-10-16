package com.mike.ui.controller;

import com.mike.shared.dto.UserDto;
import com.mike.ui.model.request.UserDetailsRequestModel;
import com.mike.ui.model.response.UserRest;
import com.mike.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser(){
        return "get user was called";
    }

    @PostMapping
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
