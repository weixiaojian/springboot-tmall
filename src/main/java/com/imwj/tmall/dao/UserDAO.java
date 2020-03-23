package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author langao_q
 * @create 2019-12-02 16:19
 * 用户dao操作类
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     * 根据name查询user
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * 根据用户名和密码查找用户
     * @param name
     * @param password
     * @return
     */
    User getByNameAndPassword(String name, String password);
}
