package com.example.aston.user.mapper;

import com.example.aston.user.dto.input.CreateUserInput;
import com.example.aston.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class CreationDtoToUserMapper {
    public static User toUserFromCreationDto(CreateUserInput dto) {
        return User.builder()
            .phone(dto.getPhone())
            .name(dto.getName())
            .email(dto.getEmail())
            .birthday(dto.getBirthday())
            .isVendor(dto.isVendor())
            .build();
    }
}