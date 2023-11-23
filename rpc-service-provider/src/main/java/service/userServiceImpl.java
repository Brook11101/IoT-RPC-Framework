package service;

import iotruleservice.IUserService;
import lombok.extern.slf4j.Slf4j;
import spring.annotation.RemoteServiceReference;

@RemoteServiceReference //表示讲当前服务发布成远程服务
@Slf4j
public class userServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        log.info("begin saveUser:"+name);
        return "Save User Success!";
    }
}
