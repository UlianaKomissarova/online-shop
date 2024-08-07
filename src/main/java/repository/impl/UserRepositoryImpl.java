package repository.impl;

import db.*;
import exception.*;
import lombok.NoArgsConstructor;
import model.User;
import repository.UserRepository;

import java.sql.Date;
import java.sql.*;
import java.util.*;

@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private static final String SAVE_USER = "INSERT into users (name, email, phone, birthday, is_vendor) VALUES (?,?,?,?,?)";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String GET_USER_BY_ID = "SELECT user_id, name, email, phone, birthday, is_vendor FROM users WHERE user_id = ?";
    private static final String GET_USERS = "SELECT user_id, name, email, phone, birthday, is_vendor FROM users";
    private static final String UPDATE_USER_BY_ID = "UPDATE users SET name = ?, email = ?, phone = ?, birthday = ?, is_vendor = ? WHERE user_id = ?";
    private static UserRepository userRepository;
    //private final ItemRepository itemRepository = ItemRepositoryImpl.getInstance();
    //private final OrderRepository orderRepository = OrderRepositoryImpl.getInstance();
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    public static synchronized UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepositoryImpl();
        }
        return userRepository;
    }

    @Override
    public User save(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {

            parseUserToDb(user, statement);
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getBirthday(),
                    user.isVendor()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return user;
    }

    @Override
    public void update(User user) throws NotFoundException {
        checkUserExists(user.getId());
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_BY_ID)) {

            parseUserToDb(user, statement);
            statement.setInt(6, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = parseUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USERS)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                users.add(parseUserFromResultSet(rs));
            }
        } catch (SQLException exception) {
            throw new RepositoryException(exception);
        }

        return users;
    }

    @Override
    public boolean deleteById(Integer id) throws NotFoundException {
        checkUserExists(id);
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID)) {

            //itemRepository.deleteVendorInItem(id);
            //orderRepository.deleteByBuyerId(id);

            statement.setInt(1, id);
            deleteResult = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    private void parseUserToDb(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPhone());
        java.sql.Date sqlDate = new Date(user.getBirthday().getTime());
        statement.setDate(4, sqlDate);
        statement.setBoolean(5, user.isVendor());
    }

    private User parseUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setBirthday(rs.getTimestamp("birthday"));
        user.setVendor(rs.getBoolean("is_vendor"));

        return user;
    }

    private void checkUserExists(int id) throws NotFoundException {
        if (!findById(id).isPresent()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
    }
}