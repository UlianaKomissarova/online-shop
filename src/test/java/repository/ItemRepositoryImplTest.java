package repository;

import db.ConnectionManager;
import exception.NotFoundException;
import model.*;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import repository.impl.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ItemRepositoryImplTest {
    private ItemRepositoryImpl itemRepository;
    private ConnectionManager connectionManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static UserRepositoryImpl userRepository;
    private static User user;

    @BeforeAll
    static void init() {
        user = new User(null, "Владимир Выготский", "adimir@mail.com", "89204305464", new Date(System.currentTimeMillis()), true);
        userRepository = (UserRepositoryImpl) UserRepositoryImpl.getInstance();
        user = userRepository.save(user);
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        itemRepository = (ItemRepositoryImpl) ItemRepositoryImpl.getInstance();
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
        Item item = new Item(null, "Елка новогодняя", "Все для праздника", true, user.getId());
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("item_id")).thenReturn(1);

        Item savedItem = itemRepository.save(item);

        assertNotNull(savedItem.getId());
        assertEquals("Елка новогодняя", savedItem.getName());
    }

    @Test
    void testUpdate() throws Exception {
        Item item = new Item(null, "Жакет женский", "Только ручная стирка", true, user.getId());
        item = itemRepository.save(item);
        Item itemForUpdate = new Item(item.getId(), "Жакет", "Любая стирка", true, user.getId());

        itemRepository.update(itemForUpdate);
        Optional<Item> foundItem = itemRepository.findById(item.getId());

        assertEquals(foundItem.get().getName(), "Жакет");
    }

    @Test
    void testFindById() throws Exception {
        Item item = new Item(null, "Зонт", "Зонт-трость прозрачный", true, user.getId());
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name")).thenReturn(item.getName());
        when(resultSet.getString("description")).thenReturn(item.getDescription());
        when(resultSet.getBoolean("is_available")).thenReturn(item.isAvailable());
        when(resultSet.getInt("vendor_id")).thenReturn(item.getVendorId());

        item = itemRepository.save(item);
        Optional<Item> foundItem = itemRepository.findById(item.getId());
        assertTrue(foundItem.isPresent());
        assertEquals(item.getName(), foundItem.get().getName());
    }

    @Test
    void testFindAll() throws Exception {
        Item item = new Item(null, "Игра настольная", "Интеллектуальная игра для компании", true, user.getId());
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn(item.getName());
        when(resultSet.getString("description")).thenReturn(item.getDescription());
        when(resultSet.getBoolean("is_available")).thenReturn(item.isAvailable());
        when(resultSet.getInt("vendor_id")).thenReturn(item.getVendorId());

        item = itemRepository.save(item);
        List<Item> items = itemRepository.findAll();
        assertEquals(1, items.size());
        assertEquals(item.getName(), items.get(0).getName());
    }

    @Test
    void testDeleteById() throws Exception {
        Item item = new Item(null, "Кружка", "350 мл", true, user.getId());

        item = itemRepository.save(item);
        int itemId = item.getId();
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = itemRepository.deleteById(itemId);
        assertTrue(result);
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        int itemId = 136;
        doThrow(new SQLException()).when(preparedStatement).executeUpdate();

        assertThrows(NotFoundException.class, () -> itemRepository.deleteById(itemId));
    }
}