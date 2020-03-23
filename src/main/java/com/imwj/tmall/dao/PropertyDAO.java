package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-19 10:36
 * 属性dao操作
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {

    /**
     * 根据分类di查询商品列表
     * @param category
     * @param pageable
     * @return
     */
    Page<Property> findByCategory(Category category, Pageable pageable);


    /**
     * 根据分类获取所有属性
     * @param category
     * @return
     */
    List<Property> findByCategory(Category category);
}
