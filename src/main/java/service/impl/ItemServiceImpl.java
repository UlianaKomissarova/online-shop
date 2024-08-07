package service.impl;

import dto.ItemDto;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.Item;
import repository.ItemRepository;
import repository.impl.ItemRepositoryImpl;
import service.ItemService;

import java.util.*;

import static service.mapper.ItemMapper.*;

@NoArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository = ItemRepositoryImpl.getInstance();
    private static ItemService instance;

    public static synchronized ItemService getInstance() {
        if (instance == null) {
            instance = new ItemServiceImpl();
        }
        return instance;
    }

    private void checkExistItem(Integer id) throws NotFoundException {
        if (!itemRepository.findById(id).isPresent()) {
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public ItemDto save(ItemDto dto) throws NotFoundException {
        Item item = itemRepository.save(toItemFromItemDto(dto));
        return toDtoFromItem(itemRepository.findById(item.getId()).orElse(item));
    }

    @Override
    public void update(ItemDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        checkExistItem(dto.getId());
        itemRepository.update(toItemFromItemDto(dto));
    }

    @Override
    public ItemDto findById(Integer id) throws NotFoundException {
        checkExistItem(id);
        Item item = itemRepository.findById(id).orElseThrow();
        return toDtoFromItem(item);
    }

    @Override
    public List<ItemDto> findAll() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> dtos = new ArrayList<>();

        for (Item item : items) {
            dtos.add(toDtoFromItem(item));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        checkExistItem(id);
        itemRepository.deleteById(id);
    }
}