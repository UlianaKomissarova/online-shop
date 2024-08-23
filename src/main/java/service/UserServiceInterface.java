package service;

import dto.UserDto;
import exception.NotFoundException;

import java.util.List;

public interface UserServiceInterface {
    void save(UserDto dto) throws NotFoundException;

    void update(UserDto user) throws NotFoundException;

    UserDto findById(Integer id) throws NotFoundException;

    List<UserDto> findAll();

    void delete(Integer id) throws NotFoundException;
}