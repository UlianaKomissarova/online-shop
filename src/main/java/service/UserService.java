package service;

import dto.*;
import exception.NotFoundException;

import java.util.List;

public interface UserService {
    UserDto save(UserDto dto) throws NotFoundException;

    void update(UserDto user) throws NotFoundException;

    UserDto findById(Integer id) throws NotFoundException;

    List<UserDto> findAll();

    void delete(Integer id) throws NotFoundException;
}