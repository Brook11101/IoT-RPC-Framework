package controller;

import iotruleservice.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.annotation.RemoteCallReference;

@RestController
public class HelloController {

    @RemoteCallReference
    private IUserService userService;


    @GetMapping("/test")
    public String test(){
        return userService.saveUser("Mic");
    }


}
