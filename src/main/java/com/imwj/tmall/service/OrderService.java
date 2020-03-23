package com.imwj.tmall.service;

import com.imwj.tmall.dao.OrderDAO;
import com.imwj.tmall.pojo.Order;
import com.imwj.tmall.pojo.OrderItem;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-03 10:51
 * 订单service操作类
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderService {

    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 订单分页数据
     * 从数据库中取出来的 Order 是没有 OrderItem集合的
     * @param start
     * @param size
     * @param navigatePages
     * @return
     */
    @Cacheable(key="'orders-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Order> list(int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Order> page = orderDAO.findAll(pageable);
        return new Page4Navigator<>(page, navigatePages);
    }

    /**
     * 清除order中orderitem中的order
     * 防止返回前台数据son序列化的时候出现递归死循环
     * @param orders
     */
    public void removeOrderFromOrderItem(List<Order> orders){
        for(Order order : orders)
            removeOrderFromOrderItem(order);
    }

    /**
     * 清除order中orderitem中的order
     * 防止返回前台数据son序列化的时候出现递归死循环
     * @param orders
     */
    public void removeOrderFromOrderItem(Order order){
        List<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem oi : orderItems)
            oi.setOrder(null);
    }

    /**
     * 根据oid获取order
     * @param oid
     * @return
     */
    @Cacheable(key="'orders-one-'+ #p0")
    public Order get(int oid){
        return orderDAO.getOne(oid);
    }

    /**
     * 更新order数据
     * @param order
     */
    @CacheEvict(allEntries=true)
    public void update(Order order){
        orderDAO.save(order);
    }

    /**
     * 增加订单数据
     * @param order
     */
    @CacheEvict(allEntries=true)
    public void add(Order order){
        orderDAO.save(order);
    }

    /**
     * 提交订单：增加订单数据，更新订单项表
     * @param o
     * @param ois
     * @return
     */
    @CacheEvict(allEntries=true)
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order o, List<OrderItem> ois) {
        float total = 0;
        add(o);

        if(false)
            throw new RuntimeException();

        for(OrderItem oi : ois){
            oi.setOrder(o);
            orderItemService.update(oi);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        return total;
    }

    /**
     * 根据用户获取订单和填充的订单项
     * @param user
     * @return
     */
    public List<Order> listByUserWithoutDelete(User user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    /**
     * 根据用户和订单状态（非删除）查询订单数据
     * @param user
     * @return
     */
    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }

    /**
     * 计算订单总金额
     * @param order
     */
    public void cacl(Order order){
        List<OrderItem> orderItems = orderItemService.listByOrder(order);
        float total = 0;
        for(OrderItem oi : orderItems){
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        order.setTotal(total);
    }

}
