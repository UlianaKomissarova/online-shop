package com.example.aston.user.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.user.dto.input.CreateUserInput;
import com.example.aston.user.dto.input.UpdateUserInput;
import com.example.aston.user.dto.output.UserOutput;
import com.example.aston.user.mapper.CreationDtoToUserMapper;
import com.example.aston.user.mapper.UserToOutputMapper;
import com.example.aston.user.model.User;
import com.example.aston.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserOutput save(CreateUserInput dto) {
        return UserToOutputMapper.toOutputFromUser(userRepository.save(CreationDtoToUserMapper.toUserFromCreationDto(dto)));
    }

    @Override
    @Transactional
    public UserOutput update(UpdateUserInput dto, Integer id) throws NotFoundException {
        User user = getExistingUser(id);
        updateUserProps(dto, user);
        user = userRepository.save(user);

        return UserToOutputMapper.toOutputFromUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserOutput findById(Integer id) throws NotFoundException {
        return UserToOutputMapper.toOutputFromUser(getExistingUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOutput> findAll() {
        return userRepository.findAll().stream()
            .map(UserToOutputMapper::toOutputFromUser)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        getExistingUser(id);
        userRepository.deleteById(id);
    }

    public User getExistingUser(Integer id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found."));
    }

    private void updateUserProps(UpdateUserInput dto, User user) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            user.setPhone(dto.getPhone());
        }

        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }

        if (dto.getIsVendor() != null) {
            user.setVendor(dto.getIsVendor());
        }
    }
}