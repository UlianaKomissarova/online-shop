package com.example.aston.user.mapper;

import com.example.aston.user.dto.output.UserOutput;
import com.example.aston.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserToOutputMapper {
    public static UserOutput toOutputFromUser(User user) {
        return UserOutput.builder()
            .birthday(user.getBirthday())
            .email(user.getEmail())
            .id(user.getId())
            .isVendor(user.isVendor())
            .name(user.getName())
            .phone(user.getPhone())
            .build();
    }
}