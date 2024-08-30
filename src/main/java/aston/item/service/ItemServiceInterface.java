package aston.item.service;

import aston.exception.NotFoundException;
import aston.item.dto.input.*;
import aston.item.dto.output.ItemOutput;

import java.util.List;

public interface ItemServiceInterface {
    ItemOutput save(CreateItemInput dto) throws NotFoundException;

    ItemOutput update(UpdateItemInput item, Integer id) throws NotFoundException;

    ItemOutput findById(Integer id) throws NotFoundException;

    List<ItemOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}