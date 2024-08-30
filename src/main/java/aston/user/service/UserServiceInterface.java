package aston.user.service;

import aston.exception.NotFoundException;
import aston.user.dto.input.*;
import aston.user.dto.output.UserOutput;

import java.util.List;

public interface UserServiceInterface {
    UserOutput save(CreateUserInput dto);

    UserOutput update(UpdateUserInput user, Integer id) throws NotFoundException;

    UserOutput findById(Integer id) throws NotFoundException;

    List<UserOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}