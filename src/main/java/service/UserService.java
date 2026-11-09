package service;

import entity.User;
import lib.errors.DataNotFoundException;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {
    private final UserRepository userRepository = new UserRepository();
    private final List<User> list;
    private static final UserService userService = new UserService();

    private UserService() {
        this.list = userRepository.read();
    }

    public static UserService getInstance() {
        return userService;
    }

    public static UserService getObject() {
        return userService;
    }

    public User findByUsername(String username) {
        for (User user : list) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> findAllByContains(String keyword) {
        List<User> listFound = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (User item : list) {
            if (item.getUsername().toLowerCase().contains(lowerKeyword) ||
                    item.getUserFullName().toLowerCase().contains(lowerKeyword)) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(User newUser) {
        this.list.add(newUser);
        this.userRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.userRepository.write(this.list);
    }

    @Override
    public void update(long id, User user) {
        int index = this.findIndexById(id);
        this.list.set(index, user);
        this.userRepository.write(this.list);
    }

    @Override
    public List<User> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            User user = this.list.get(i);
            if (user.getUserId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("User not found with ID: " + id);
    }

    @Override
    public User findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
