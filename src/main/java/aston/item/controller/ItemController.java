package aston.item.controller;

import aston.exception.NotFoundException;
import aston.item.dto.input.*;
import aston.item.dto.output.ItemOutput;
import aston.item.service.ItemServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceInterface itemService;

    @GetMapping
    public Collection<ItemOutput> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ItemOutput findById(@PathVariable("id") Integer id) throws NotFoundException {
        return itemService.findById(id);
    }

    @PostMapping
    public ItemOutput save(@RequestBody CreateItemInput dto) throws NotFoundException {
        return itemService.save(dto);
    }

    @PatchMapping("/{id}")
    public ItemOutput update(@RequestBody UpdateItemInput dto, @PathVariable("id") Integer id) throws NotFoundException {
        return itemService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) throws NotFoundException {
        itemService.deleteById(id);
    }
}