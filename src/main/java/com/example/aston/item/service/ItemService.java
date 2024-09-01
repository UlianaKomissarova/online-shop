package com.example.aston.item.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.item.dto.input.CreateItemInput;
import com.example.aston.item.dto.input.UpdateItemInput;
import com.example.aston.item.dto.output.ItemOutput;
import com.example.aston.item.mapper.ItemToOutputMapper;
import com.example.aston.item.model.Item;
import com.example.aston.item.repository.ItemRepository;
import com.example.aston.user.model.User;
import com.example.aston.user.service.UserService;
import com.example.aston.item.mapper.CreationDtoToItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemOutput save(CreateItemInput dto) throws NotFoundException {
        Item item = CreationDtoToItemMapper.toItemFromCreationDto(dto);
        setItemVendor(dto.getVendorId(), item);
        item = itemRepository.save(item);

        return ItemToOutputMapper.toOutputFromItem(item);
    }

    @Override
    @Transactional
    public ItemOutput update(UpdateItemInput dto, Integer id) throws NotFoundException {
        Item item = getExistingItem(id);
        updateItemProps(dto, item);
        item = itemRepository.save(item);

        return ItemToOutputMapper.toOutputFromItem(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemOutput findById(Integer id) throws NotFoundException {
        return ItemToOutputMapper.toOutputFromItem(getExistingItem(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemOutput> findAll() {
        return itemRepository.findAll().stream()
            .map(ItemToOutputMapper::toOutputFromItem)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        getExistingItem(id);
        itemRepository.deleteById(id);
    }

    public Item getExistingItem(Integer id) throws NotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found."));
    }

    private void updateItemProps(UpdateItemInput dto, Item item) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            item.setPrice(dto.getPrice());
        }

        if (dto.getQuantity() >= 0) {
            item.setQuantity(dto.getQuantity());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }

        if (dto.getIsAvailable() != null) {
            item.setAvailable(dto.getIsAvailable());
        }
    }

    private void setItemVendor(int vendorId, Item item) throws NotFoundException {
        User vendor = userService.getExistingUser(vendorId);
        item.setVendor(vendor);
    }
}