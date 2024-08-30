package aston.user.mapper;

import aston.user.dto.input.CreateUserInput;
import aston.user.model.User;
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