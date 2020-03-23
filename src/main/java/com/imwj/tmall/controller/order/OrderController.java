package com.imwj.tmall.controller.order;

import com.imwj.tmall.pojo.Order;
import com.imwj.tmall.service.OrderItemService;
import com.imwj.tmall.service.OrderService;
import com.imwj.tmall.util.Page4Navigator;
import com.imwj.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author langao_q
 * @create 2019-12-03 11:01
 * 订单controller操作类
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 订单分页数据
     * @param start
     * @param size
     * @return
     */
    @GetMapping(value = "/orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start", defaultValue = "0")int start,
                                      @RequestParam(value = "size", defaultValue = "5")int size){
        start = start<0?0:start;
        Page4Navigator<Order> list = orderService.list(start, size, 5);
        orderItemService.fill(list.getContent());
        orderService.removeOrderFromOrderItem(list.getContent());
        return list;
    }

    /**
     * 订单发货
     * @param oid
     * @return
     */
    @PutMapping(value = "/deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable int oid){
        Order order = orderService.get(oid);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.waitConfirm);
        orderService.update(order);
        return Result.success();
    }

}
