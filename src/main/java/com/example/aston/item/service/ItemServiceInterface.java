package com.example.aston.item.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.item.dto.input.CreateItemInput;
import com.example.aston.item.dto.input.UpdateItemInput;
import com.example.aston.item.dto.output.ItemOutput;

import java.util.List;

public interface ItemServiceInterface {
    ItemOutput save(CreateItemInput dto) throws NotFoundException;

    ItemOutput update(UpdateItemInput item, Integer id) throws NotFoundException;

    ItemOutput findById(Integer id) throws NotFoundException;

    List<ItemOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}