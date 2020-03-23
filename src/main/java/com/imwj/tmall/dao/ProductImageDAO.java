package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-25 16:42
 * 商品图片dao操作
 */
public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {

    /**
     * 根据商品id查询商品图片列表
     * @param product
     * @param type
     * @return
     */
    public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);


}
