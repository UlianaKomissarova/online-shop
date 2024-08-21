package service.impl;

import dto.ItemDto;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.*;
import repository.impl.ItemDao;
import service.ItemServiceInterface;

import java.util.*;

import static service.mapper.ItemMapper.*;

@NoArgsConstructor
public class ItemService implements ItemServiceInterface {
    private final ItemDao dao = new ItemDao();
    private static ItemServiceInterface itemService;
    private final UserService userService = (UserService) UserService.getInstance();

    public static synchronized ItemServiceInterface getInstance() {
        if (itemService == null) {
            itemService = new ItemService();
        }
        return itemService;
    }

    @Override
    public void save(ItemDto dto) throws NotFoundException {
        Item item = toItemFromDto(dto);
        setItemVendor(dto.getVendorId(), item);

        dao.save(item);
    }

    @Override
    public void update(ItemDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        getExistingItem(dto.getId());

        Item itemForUpdate = toItemFromDto(dto);
        setItemVendor(dto.getVendorId(), itemForUpdate);

        dao.update(itemForUpdate);
    }

    @Override
    public ItemDto findById(Integer id) throws NotFoundException {
        Item item = getExistingItem(id);
        return toDtoFromItem(item);
    }

    @Override
    public List<ItemDto> findAll() {
        List<Item> items = dao.findAll();
        List<ItemDto> dtos = new ArrayList<>();

        for (Item item : items) {
            dtos.add(toDtoFromItem(item));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        Item item = getExistingItem(id);
        dao.delete(item);
    }

    protected Item getExistingItem(Integer id) throws NotFoundException {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("Item not found."));
    }

    public void setItemVendor(int vendorId, Item item) throws NotFoundException {
        User vendor = userService.getExistingUser(vendorId);
        item.setVendor(vendor);
    }
}