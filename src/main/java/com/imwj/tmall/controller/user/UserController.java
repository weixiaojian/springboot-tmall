package com.imwj.tmall.controller.user;

import com.imwj.tmall.pojo.User;
import com.imwj.tmall.service.UserService;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author langao_q
 * @create 2019-12-02 16:23
 * 用户controller操作类
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public Page4Navigator<User> list(@RequestParam(value = "start", defaultValue = "0")int start,
                                     @RequestParam(value = "size", defaultValue = "5")int size){
        start = start<0?0:start;
        Page4Navigator<User> list = userService.list(start, size, 5);
        return list;
    }
}
