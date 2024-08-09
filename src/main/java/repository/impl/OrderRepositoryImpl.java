package repository.impl;

import db.*;
import exception.*;
import lombok.NoArgsConstructor;
import model.*;
import repository.*;

import java.sql.Date;
import java.sql.*;
import java.util.*;

@NoArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private static final String SAVE_ORDER = "INSERT into orders (item_id, buyer_id, status, created) VALUES (?,?,?,?)";
    private static final String GET_ORDER_BY_ID = "SELECT order_id, item_id, buyer_id, status, created FROM orders WHERE order_id = ?";
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE order_id = ?";
    private static final String GET_ORDERS = "SELECT order_id, item_id, buyer_id, status, created FROM orders";
    private static final String UPDATE_ORDER_BY_ID = "UPDATE orders SET item_id = ?, buyer_id = ?, status = ? WHERE order_id = ?";

    private static final String DELETE_BY_BUYER_ID = "DELETE FROM orders WHERE buyer_id = ?";
    private static final String DELETE_BY_ITEM_ID = "DELETE FROM orders WHERE item_id = ?";
    private static OrderRepository orderRepository;
    private static final ItemRepository itemRepository = new ItemRepositoryImpl();
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public static synchronized OrderRepository getInstance() {
        if (orderRepository == null) {
            orderRepository = new OrderRepositoryImpl();
        }
        return orderRepository;
    }

    @Override
    public Order save(Order order) throws NotFoundException {
        checkItemForOrder(order);
        checkUserForOrder(order);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            parseOrderToDb(order, statement);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                order = new Order(
                    rs.getInt(1),
                    order.getItemId(),
                    order.getBuyerId(),
                    //order.getStatus(),
                    OrderStatus.IN_PROCESS,
                    order.getCreatedAt()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return order;
    }

    @Override
    public void update(Order order) throws NotFoundException {
        checkOrderExists(order.getId());
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_BY_ID)) {

            parseOrderToDb(order, statement);
            statement.setInt(4, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) throws NotFoundException {
        checkOrderExists(id);
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_BY_ID)) {

            statement.setInt(1, id);
            deleteResult = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public Optional<Order> findById(Integer id) {
        Order order = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ORDER_BY_ID)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                order = parseOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ORDERS)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                orders.add(parseOrderFromResultSet(rs));
            }
        } catch (SQLException exception) {
            throw new RepositoryException(exception);
        }

        return orders;
    }

    @Override
    public boolean deleteByBuyerId(int id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_BUYER_ID)) {

            preparedStatement.setInt(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public boolean deleteByItemId(int id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ITEM_ID)) {

            preparedStatement.setInt(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    private void parseOrderToDb(Order order, PreparedStatement statement) throws SQLException {
        statement.setInt(1, order.getItemId());
        statement.setInt(2, order.getBuyerId());
        statement.setString(3, order.getStatus().toString());
        java.sql.Date sqlDate = new Date(order.getCreatedAt().getTime());
        statement.setDate(4, sqlDate);
    }

    private Order parseOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("order_id"));
        order.setItemId(rs.getInt("item_id"));
        order.setBuyerId(rs.getInt("buyer_id"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        java.util.Date utilDate = new java.util.Date(rs.getDate("created").getTime());
        order.setCreatedAt(utilDate);

        return order;
    }

    private void checkOrderExists(int id) throws NotFoundException {
        if (!findById(id).isPresent()) {
            throw new NotFoundException("Заказ с id " + id + " не найден.");
        }
    }

    private void checkItemForOrder(Order order) throws NotFoundException {
        Optional<Item> item = itemRepository.findById(order.getItemId());
        if (!item.isPresent()) {
            throw new NotFoundException("Товар из заказа не найден.");
        }

        boolean isAvailable = item.get().isAvailable();
        if (!isAvailable) {
            throw new RuntimeException("Товар недоступен для заказа.");
        }
    }

    private void checkUserForOrder(Order order) throws NotFoundException {
        Optional<User> user = userRepository.findById(order.getBuyerId());
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь из заказа не найден.");
        }

        Optional<Item> item = itemRepository.findById(order.getItemId());
        int buyerId = user.get().getId();
        int vendorId = item.get().getVendorId();
        if (buyerId == vendorId) {
            throw new RuntimeException("Продавец не может купить свою вещь.");
        }
    }
}