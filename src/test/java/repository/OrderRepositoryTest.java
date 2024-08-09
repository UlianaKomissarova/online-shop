package repository;

import db.ConnectionManager;
import exception.NotFoundException;
import model.Order;
import model.*;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import repository.impl.*;

import java.sql.Date;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderRepositoryTest {
    private OrderRepositoryImpl orderRepository;
    private static ItemRepositoryImpl itemRepository;
    private static UserRepositoryImpl userRepository;
    private ConnectionManager connectionManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static User user;
    private static User itemOwner;
    private static Item item;
    private static Order order;

    @BeforeAll
    static void init() {
        userRepository = (UserRepositoryImpl) UserRepositoryImpl.getInstance();
        itemOwner = new User(null, "Owner", "owner@mail.com", "89204105464", new Date(System.currentTimeMillis()), true);
        itemOwner = userRepository.save(itemOwner);

        user = new User(null, "User", "user@mail.com", "89104105464", new Date(System.currentTimeMillis()), true);
        user = userRepository.save(user);

        itemRepository = (ItemRepositoryImpl) ItemRepositoryImpl.getInstance();
        item = new Item(null, "Ложка", "Столовая", true, itemOwner.getId());
        item = itemRepository.save(item);
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        orderRepository = (OrderRepositoryImpl) OrderRepositoryImpl.getInstance();
        connectionManager = mock(ConnectionManager.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    void testSave() throws Exception {
        order = new Order(null, item.getId(), user.getId(), OrderStatus.IN_PROCESS, new Date(System.currentTimeMillis()));
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("order_id")).thenReturn(1);

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals(item.getId(), savedOrder.getItemId());
    }

    @Test
    void testUpdate() throws Exception {
        order = new Order(null, item.getId(), user.getId(), OrderStatus.IN_PROCESS, new Date(System.currentTimeMillis()));
        order = orderRepository.save(order);
        Order orderForUpdate = new Order(order.getId(), item.getId(), user.getId(), OrderStatus.DELIVERED, new Date(System.currentTimeMillis()));

        orderRepository.update(orderForUpdate);
        Optional<Order> foundOrder = orderRepository.findById(order.getId());

        assertEquals(foundOrder.get().getStatus(), OrderStatus.DELIVERED);
    }

    @Test
    void testFindById() throws Exception {
        order = new Order(null, item.getId(), user.getId(), OrderStatus.IN_PROCESS, new Date(System.currentTimeMillis()));
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("item_id")).thenReturn(order.getItemId());
        when(resultSet.getInt("buyer_id")).thenReturn(order.getBuyerId());
        when(resultSet.getString("status")).thenReturn(order.getStatus().toString());
        when(resultSet.getDate("created")).thenReturn((Date) order.getCreatedAt());

        order = orderRepository.save(order);
        Optional<Order> foundOrder = orderRepository.findById(order.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals(order.getStatus(), foundOrder.get().getStatus());
    }

    @Test
    void testFindAll() throws Exception {
        order = new Order(null, item.getId(), user.getId(), OrderStatus.IN_PROCESS, new Date(System.currentTimeMillis()));
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("item_id")).thenReturn(order.getItemId());
        when(resultSet.getInt("buyer_id")).thenReturn(order.getBuyerId());
        when(resultSet.getString("status")).thenReturn(order.getStatus().toString());
        when(resultSet.getDate("created")).thenReturn((Date) order.getCreatedAt());

        order = orderRepository.save(order);
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
        assertEquals(order.getStatus(), orders.get(0).getStatus());
    }

    @Test
    void testDeleteById() throws Exception {
        order = new Order(null, item.getId(), user.getId(), OrderStatus.IN_PROCESS, new Date(System.currentTimeMillis()));
        order = orderRepository.save(order);
        int orderId = order.getId();
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = orderRepository.deleteById(orderId);
        assertTrue(result);
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        int orderId = 136;
        doThrow(new SQLException()).when(preparedStatement).executeUpdate();

        assertThrows(NotFoundException.class, () -> orderRepository.deleteById(orderId));
    }
}