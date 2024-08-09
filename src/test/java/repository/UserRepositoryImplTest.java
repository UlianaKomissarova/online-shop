package repository;

import db.ConnectionManager;
import exception.NotFoundException;
import model.User;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import repository.impl.UserRepositoryImpl;

import java.sql.Date;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;
    private ConnectionManager connectionManager;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userRepository = (UserRepositoryImpl) UserRepositoryImpl.getInstance();
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
        User user = new User(null, "Алиса Анохина", "alisa@mail.ru", "89203127866", new Date(System.currentTimeMillis()), false);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("user_id")).thenReturn(1);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Алиса Анохина", savedUser.getName());
    }

    @Test
    void testUpdate() throws Exception {
        User user = new User(null, "Борис Бунин", "boris@mail.com", "89303036754", new Date(System.currentTimeMillis()), true);
        user = userRepository.save(user);
        User userForUpdate = new User(user.getId(), "Борис Обновленный", "boriss@mail.com", "89303036754", new Date(System.currentTimeMillis()), true);

        userRepository.update(userForUpdate);
        Optional<User> foundUser = userRepository.findById(user.getId());

        assertEquals(foundUser.get().getName(), "Борис Обновленный");
    }

    @Test
    void testFindById() throws Exception {
        User user = new User(null, "Владимир Выготский", "vladimir@mail.com", "89204305464", new Date(System.currentTimeMillis()), true);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("name")).thenReturn(user.getName());
        when(resultSet.getString("email")).thenReturn(user.getEmail());
        when(resultSet.getString("phone")).thenReturn(user.getPhone());
        when(resultSet.getDate("birthday")).thenReturn((Date) user.getBirthday());
        when(resultSet.getBoolean("is_vendor")).thenReturn(user.isVendor());

        user = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    void testFindAll() throws Exception {
        User user = new User(null, "Гена Горин", "gena@mail.ru", "89303404546", new Date(System.currentTimeMillis()), true);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenReturn(user.getName());
        when(resultSet.getString("email")).thenReturn(user.getEmail());
        when(resultSet.getString("phone")).thenReturn(user.getPhone());
        when(resultSet.getDate("birthday")).thenReturn((Date) user.getBirthday());
        when(resultSet.getBoolean("is_vendor")).thenReturn(user.isVendor());

        userRepository.save(user);
        List<User> users = userRepository.findAll();
        assertTrue(users.size() > 0);
    }

    @Test
    void testDeleteById() throws Exception {
        User user = new User(null, "Кристина Кей", "key@mail.ru", "89303404846", new Date(System.currentTimeMillis()), true);

        user = userRepository.save(user);
        int userId = user.getId();
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userRepository.deleteById(userId);
        assertTrue(result);
    }

    @Test
    void testDeleteByIdNotFound() throws Exception {
        int userId = 136;
        doThrow(new SQLException()).when(preparedStatement).executeUpdate();

        assertThrows(NotFoundException.class, () -> userRepository.deleteById(userId));
    }
}