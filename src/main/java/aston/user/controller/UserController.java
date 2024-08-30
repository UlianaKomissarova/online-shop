package aston.user.controller;

import aston.exception.NotFoundException;
import aston.user.dto.input.*;
import aston.user.dto.output.UserOutput;
import aston.user.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceInterface userService;

    @GetMapping
    public Collection<UserOutput> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserOutput findById(@PathVariable("id") Integer id) throws NotFoundException {
        return userService.findById(id);
    }

    @PostMapping
    public UserOutput save(@RequestBody CreateUserInput dto) throws NotFoundException {
        return userService.save(dto);
    }

    @PatchMapping("/{id}")
    public UserOutput update(@RequestBody UpdateUserInput dto, @PathVariable("id") Integer id) throws NotFoundException {
        return userService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) throws NotFoundException {
        userService.deleteById(id);
    }
}