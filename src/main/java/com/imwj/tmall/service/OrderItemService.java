package com.imwj.tmall.service;

import com.imwj.tmall.dao.OrderItemDAO;
import com.imwj.tmall.pojo.Order;
import com.imwj.tmall.pojo.OrderItem;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.User;
import com.imwj.tmall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-03 10:44
 * 订单项service类
 */
@Service
@CacheConfig(cacheNames="orderItems")
public class OrderItemService {

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private ProductImageService productImageService;

    /**
     * 计算订单的总数和总价
     * 填充订单项列表数据
     * @param orders
     */
    public void fill(List<Order> orders){
        for(Order order : orders)
            fill(order);
    }
    public void fill(Order order){
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for(OrderItem oi : orderItems){
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
            productImageService.setFirstProdutImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }

    /**
     * 根据订单查询订单项数据
     * @param order
     * @return
     */
    @Cacheable(key="'orderItems-oid-'+ #p0.id")
    public List<OrderItem> listByOrder(Order order){
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

    /**
     * 商品销售统计
     * @param product
     * @return
     */
    public int getSaleCount(Product product){
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> orderItems = orderItemService.listByProduct(product);
        int result = 0;
        for(OrderItem oi : orderItems){
            if(null != oi.getOrder())
                if(null != oi.getOrder() && null != oi.getOrder().getPayDate())
                    result += oi.getNumber();
        }
        return result;
    }

    /**
     * 根据商品查询订单项集合
     * @param product
     * @return
     */
    @Cacheable(key="'orderItems-pid-'+ #p0.id")
    public List<OrderItem> listByProduct(Product product){
        return orderItemDAO.findByProduct(product);
    }

    /**
     * 根据user查询对应的订单项
     * @param user
     * @return
     */
    @Cacheable(key="'orderItems-uid-'+ #p0.id")
    public List<OrderItem> listByUser(User user){
        return orderItemDAO.findByUserAndOrderIsNull(user);
    }

    /**
     * 更新orderitem数据
     * @param oi
     */
    @CacheEvict(allEntries=true)
    public void update(OrderItem oi) {
        orderItemDAO.save(oi);
    }

    /**
     * 增加orderitem数据
     * @param oi
     */
    @CacheEvict(allEntries=true)
    public void save(OrderItem oi) {
        orderItemDAO.save(oi);
    }

    /**
     * 根据id查询orderitem数据
     * @param oiid
     * @return
     */
    @Cacheable(key="'orderItems-one-'+ #p0")
    public OrderItem get(Integer oiid) {
        return orderItemDAO.getOne(oiid);
    }

    /**
     * 根据id删除orderitem数据
     * @param oiid
     */
    public void delete(Integer oiid) {
        orderItemDAO.delete(oiid);
    }

}
