package service.mapper;

import dto.UserDto;
import model.User;

public class UserMapper {
    public static User toUserFromDto(UserDto dto) {
        return User.builder()
            .id(dto.getId())
            .phone(dto.getPhone())
            .name(dto.getName())
            .email(dto.getEmail())
            .birthday(dto.getBirthday())
            .isVendor(dto.isVendor())
            .build();
    }

    public static UserDto toDtoFromUser(User user) {
        return UserDto.builder()
            .birthday(user.getBirthday())
            .email(user.getEmail())
            .id(user.getId())
            .isVendor(user.isVendor())
            .name(user.getName())
            .phone(user.getPhone())
            .build();
    }
}