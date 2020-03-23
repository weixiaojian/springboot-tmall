package com.imwj.tmall.controller.property;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.PropertyValue;
import com.imwj.tmall.service.ProductService;
import com.imwj.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-02 15:10
 * 属性值控制类
 */
@RestController
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ProductService productService;

    /**
     * 属性值列表
     * @param pid
     * @return
     */
    @GetMapping(value = "products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable Integer pid){
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> list = propertyValueService.list(product);
        return  list;
    }

    /**
     * 更新属性值
     * @param bean
     * @return
     */
    @PutMapping("propertyValues")
    public Object update(@RequestBody PropertyValue bean){
        propertyValueService.update(bean);
        return bean;
    }
}
