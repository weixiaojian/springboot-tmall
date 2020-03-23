package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Order;
import com.imwj.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-03 10:40
 * 订单操作类
 */
public interface OrderDAO extends JpaRepository<Order, Integer> {

    /**
     * 根据用户和状态查询订单数据
     * @param user
     * @param status
     * @return
     */
    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}
