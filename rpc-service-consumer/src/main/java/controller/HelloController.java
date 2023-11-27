package controller;

import iotruleservice.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.annotation.RemoteCallReference;

import java.util.Map;

@RestController
public class HelloController {

    @RemoteCallReference
    private IUserService userService;

    @GetMapping("getUser")
    public String getUser(@RequestParam(name = "id") String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("getUsers")
    public Map<String, User> getUsers() {
        return userService.getUsers();
    }


}
