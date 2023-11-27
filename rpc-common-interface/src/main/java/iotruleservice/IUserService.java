package iotruleservice;

import java.util.Map;

public interface IUserService {
    String getUser(String userId);
    Map<String, User> getUsers();
}
