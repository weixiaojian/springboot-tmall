package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.Property;
import com.imwj.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-02 14:34
 * 属性值dao类
 */
public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {

    /**
     * 根据商品获取属性值列表
     * @param product
     * @return
     */
    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    /**
     * 根据属性和商品获取指定属性值
     * @param property
     * @param product
     * @return
     */
    PropertyValue getByPropertyAndProduct(Property property, Product product);
}
