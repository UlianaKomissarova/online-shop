package repository.impl;

import db.*;
import exception.*;
import lombok.NoArgsConstructor;
import model.Item;
import repository.ItemRepository;

import java.sql.*;
import java.util.*;

@NoArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private static final String SAVE_ITEM = "INSERT into items (name, description, is_available, vendor_id) VALUES (?,?,?,?)";
    private static final String DELETE_ITEM_BY_ID = "DELETE FROM items WHERE item_id = ?";
    private static final String GET_ITEM_BY_ID = "SELECT item_id, name, description, is_available, vendor_id FROM items WHERE item_id = ?";
    private static final String GET_ITEMS = "SELECT item_id, name, description, is_available, vendor_id FROM items";
    private static final String UPDATE_ITEM_BY_ID = "UPDATE items SET name = ?, description = ?, is_available = ?, vendor_id = ? WHERE item_id = ?";
    private static final String DELETE_VENDOR_IN_ITEM = "DELETE FROM items WHERE vendor_id = ?";
    private static ItemRepository itemRepository;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public static synchronized ItemRepository getInstance() {
        if (itemRepository == null) {
            itemRepository = new ItemRepositoryImpl();
        }
        return itemRepository;
    }

    @Override
    public Item save(Item item) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_ITEM, Statement.RETURN_GENERATED_KEYS)) {

            parseItemToDb(item, statement);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                item = new Item(
                    rs.getInt("item_id"),
                    item.getName(),
                    item.getDescription(),
                    item.isAvailable(),
                    item.getVendorId()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return item;
    }

    @Override
    public boolean deleteById(Integer id) throws NotFoundException {
        checkItemExists(id);
        boolean deleteResult;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ITEM_BY_ID)) {

            statement.setInt(1, id);
            deleteResult = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public Optional<Item> findById(Integer id) {
        Item item = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITEM_BY_ID)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                item = parseItemFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ITEMS)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(parseItemFromResultSet(rs));
            }
        } catch (SQLException exception) {
            throw new RepositoryException(exception);
        }

        return items;
    }

    @Override
    public void update(Item item) throws NotFoundException {
        checkItemExists(item.getId());
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM_BY_ID)) {

            parseItemToDb(item, statement);
            statement.setInt(5, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteVendorInItem(int id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_VENDOR_IN_ITEM)) {

            preparedStatement.setLong(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    private void parseItemToDb(Item item, PreparedStatement statement) throws SQLException {
        statement.setString(1, item.getName());
        statement.setString(2, item.getDescription());
        statement.setBoolean(3, item.isAvailable());
        statement.setInt(4, item.getVendorId());
    }

    private Item parseItemFromResultSet(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("item_id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setAvailable(rs.getBoolean("is_available"));
        item.setVendorId(rs.getInt("vendor_id"));

        return item;
    }

    private void checkItemExists(int id) throws NotFoundException {
        if (!findById(id).isPresent()) {
            throw new NotFoundException("Товар с id " + id + " не найден.");
        }
    }
}