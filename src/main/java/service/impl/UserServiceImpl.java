package service.impl;

import dto.UserDto;
import exception.NotFoundException;
import model.User;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;
import service.UserService;

import java.util.*;

import static service.mapper.UserMapper.*;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private static UserService instance;

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    private void checkExistUser(Integer id) throws NotFoundException {
        if (!userRepository.findById(id).isPresent()) {
            throw new NotFoundException("User not found.");
        }
    }

    @Override
    public UserDto save(UserDto dto) throws NotFoundException {
        User user = userRepository.save(toUserFromDto(dto));
        return toDtoFromUser(userRepository.findById(user.getId()).orElse(user));
    }

    @Override
    public void update(UserDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        checkExistUser(dto.getId());
        userRepository.update(toUserFromDto(dto));
    }

    @Override
    public UserDto findById(Integer id) throws NotFoundException {
        checkExistUser(id);
        User user = userRepository.findById(id).orElseThrow();
        return toDtoFromUser(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();

        for (User user : users) {
            dtos.add(toDtoFromUser(user));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        checkExistUser(id);
        userRepository.deleteById(id);
    }
}