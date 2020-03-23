package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-24 14:24
 */
public interface ProductDAO extends JpaRepository<Product, Integer> {

    /**
     * 根据分类di查询商品列表
     * @param category
     * @param pageable
     * @return
             */
    Page<Product> findByCategory(Category category, Pageable pageable);

    /**
     * 根据分类查询该分类下的所有商品
     * @return
     */
    public List<Product> findByCategoryOrderById(Category category);

    /**
     * 根据商品名称模糊查询
     * @param name
     * @param pageable
     * @return
     */
    public List<Product> findByNameLike(String name, Pageable pageable);
}
