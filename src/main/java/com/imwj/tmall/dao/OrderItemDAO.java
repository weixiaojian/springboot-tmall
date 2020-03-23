package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Order;
import com.imwj.tmall.pojo.OrderItem;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-03 10:20
 * 订单项dao操作类
 */
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {

    /**
     * 根据订单查询订单项列表
     * @param order
     * @return
     */
    List<OrderItem> findByOrderOrderByIdDesc(Order order);

    /**
     * 根据商品查询订单项集合
     * @param product
     * @return
     */
    List<OrderItem> findByProduct(Product product);

    /**
     * 根据用户查询对应的订单项
     * @param user
     * @return
     */
    List<OrderItem> findByUserAndOrderIsNull(User user);
}
