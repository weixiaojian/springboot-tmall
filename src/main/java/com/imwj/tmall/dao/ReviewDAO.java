package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-06 15:23
 * 评论dao操作类
 */

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    /**
     * 根据商品查询所有对应评论
     * @param product
     * @return
     */
    List<Review> findByProductOrderByIdDesc(Product product);

    /**
     * 根据商品统计评论总数
     * @param product
     * @return
     */
    int countByProduct(Product product);
}
