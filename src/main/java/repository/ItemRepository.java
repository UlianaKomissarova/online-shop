package repository;

import model.Item;

public interface ItemRepository extends Repository<Item, Integer> {
    boolean deleteVendorInItem(int id);
}