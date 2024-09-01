package com.example.aston.user.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.user.dto.input.CreateUserInput;
import com.example.aston.user.dto.input.UpdateUserInput;
import com.example.aston.user.dto.output.UserOutput;

import java.util.List;

public interface UserServiceInterface {
    UserOutput save(CreateUserInput dto);

    UserOutput update(UpdateUserInput user, Integer id) throws NotFoundException;

    UserOutput findById(Integer id) throws NotFoundException;

    List<UserOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}