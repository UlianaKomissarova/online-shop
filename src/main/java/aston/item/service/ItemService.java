package aston.item.service;

import aston.exception.NotFoundException;
import aston.item.dto.input.*;
import aston.item.dto.output.ItemOutput;
import aston.item.mapper.ItemToOutputMapper;
import aston.item.model.Item;
import aston.item.repository.ItemRepository;
import aston.user.model.User;
import aston.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static aston.item.mapper.CreationDtoToItemMapper.toItemFromCreationDto;
import static aston.item.mapper.ItemToOutputMapper.toOutputFromItem;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemOutput save(CreateItemInput dto) throws NotFoundException {
        Item item = toItemFromCreationDto(dto);
        setItemVendor(dto.getVendorId(), item);
        item = itemRepository.save(item);

        return toOutputFromItem(item);
    }

    @Override
    @Transactional
    public ItemOutput update(UpdateItemInput dto, Integer id) throws NotFoundException {
        Item item = getExistingItem(id);
        updateItemProps(dto, item);
        item = itemRepository.save(item);

        return toOutputFromItem(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemOutput findById(Integer id) throws NotFoundException {
        return toOutputFromItem(getExistingItem(id));
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