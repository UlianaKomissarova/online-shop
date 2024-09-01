package com.example.aston.user.controller;

import com.example.aston.exception.NotFoundException;
import com.example.aston.user.dto.input.CreateUserInput;
import com.example.aston.user.dto.input.UpdateUserInput;
import com.example.aston.user.dto.output.UserOutput;
import com.example.aston.user.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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