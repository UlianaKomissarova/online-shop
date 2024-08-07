package service;

import dto.ItemDto;
import exception.NotFoundException;

import java.util.List;

public interface ItemService {
    ItemDto save(ItemDto dto) throws NotFoundException;

    void update(ItemDto item) throws NotFoundException;

    ItemDto findById(Integer id) throws NotFoundException;

    List<ItemDto> findAll();

    void delete(Integer id) throws NotFoundException;
}