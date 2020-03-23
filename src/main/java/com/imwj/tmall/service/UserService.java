package com.imwj.tmall.service;

import com.imwj.tmall.dao.UserDAO;
import com.imwj.tmall.pojo.User;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author langao_q
 * @create 2019-12-02 16:20
 * 用户service操作
 */
@Service
@CacheConfig(cacheNames="users")
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * 用户分页数据
     * @param start
     * @param size
     * @param navigatePages
     * @return
     */
    @Cacheable(key="'users-page-'+#p0+ '-' + #p1")
    public Page4Navigator<User> list(int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<User> page = userDAO.findAll(pageable);
        return new Page4Navigator<>(page, navigatePages);
    }

    /**
     * 校验用户名是否存在
     * @param name
     * @return
     */
    public boolean isExis(String name){
        User user = userDAO.findByName(name);
        return null!=user;
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @CacheEvict(allEntries=true)
    public User add(User user){
        return userDAO.save(user);
    }

    /**
     * 根据用户名和密码获取用户数据
     * @param name
     * @param password
     * @return
     */
    @Cacheable(key="'users-one-name-'+ #p0 +'-password-'+ #p1")
    public User get(String name, String password){
        return userDAO.getByNameAndPassword(name, password);
    }

    /**
     * 根据用户名获取用户数据
     * @param username
     * @return
     */
    @Cacheable(key="'users-one-name-'+ #p0")
    public User getByName(String username) {
        return userDAO.findByName(username);
    }
}
