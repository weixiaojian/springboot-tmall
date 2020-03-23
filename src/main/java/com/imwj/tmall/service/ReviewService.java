package com.imwj.tmall.service;

import com.imwj.tmall.dao.ReviewDAO;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-06 15:27
 * 评论service操作类
 */
@Service
@CacheConfig(cacheNames="reviews")
public class ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    /**
     * 添加评论
     * @param review
     */
    @CacheEvict(allEntries=true)
    public void add(Review review) {
        reviewDAO.save(review);
    }

    /**
     * 查询商品下对应的评论
     * @param product
     * @return
     */
    @Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product){
        return reviewDAO.findByProductOrderByIdDesc(product);
    }

    /**
     * 统计商品的评论总数
     * @param product
     * @return
     */
    @Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product){
        return reviewDAO.countByProduct(product);
    }
}
