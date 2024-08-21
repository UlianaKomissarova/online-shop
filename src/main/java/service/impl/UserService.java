package service.impl;

import dto.UserDto;
import exception.NotFoundException;
import model.User;
import repository.impl.UserDao;
import service.UserServiceInterface;

import java.util.*;

import static service.mapper.UserMapper.*;

public class UserService implements UserServiceInterface {
    private final UserDao dao = new UserDao();
    private static UserServiceInterface service;

    public static synchronized UserServiceInterface getInstance() {
        if (service == null) {
            service = new UserService();
        }
        return service;
    }

    @Override
    public void save(UserDto dto) throws NotFoundException {
        dao.save(toUserFromDto(dto));
    }

    @Override
    public void update(UserDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }

        getExistingUser(dto.getId());
        dao.update(toUserFromDto(dto));
    }

    @Override
    public UserDto findById(Integer id) throws NotFoundException {
        User user = getExistingUser(id);
        return toDtoFromUser(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = dao.findAll();
        List<UserDto> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(toDtoFromUser(user));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        User user = getExistingUser(id);
        dao.delete(user);
    }

    protected User getExistingUser(Integer id) throws NotFoundException {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("User not found."));
    }
}