package com.imwj.tmall.service;

import com.imwj.tmall.dao.ProductImageDAO;
import com.imwj.tmall.pojo.OrderItem;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.ProductImage;
import com.imwj.tmall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-25 16:46
 * 商品图片service操作类
 */
@Service
@CacheConfig(cacheNames="productImages")
public class ProductImageService {

    @Autowired
    private ProductImageDAO productImageDAO;

    @Autowired
    private ProductService productService;

    public static final String type_single = "single";//商品缩略图
    public static final String type_detail = "detail";//商品详情图

    /**
     * 商品缩略图集合数据
     * @param product
     * @return
     */
    @Cacheable(key="'productImages-single-pid-'+ #p0.id")
    public List<ProductImage> listSingleProductImages(Product product){
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_single);
    }

    /**
     * 商品详情图集合数据
     * @param product
     * @return
     */
    @Cacheable(key="'productImages-detail-pid-'+ #p0.id")
    public List<ProductImage> listDetailProductImages(Product product){
        return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_detail);
    }

    /**
     * 增加商品图片
     * @param productImage
     * @return
     */
    @CacheEvict(allEntries=true)
    public ProductImage add(ProductImage productImage){
        return productImageDAO.save(productImage);
    }

    /**
     * 删除商品图片
     * @param id
     */
    @CacheEvict(allEntries=true)
    public void delete(Integer id){
        productImageDAO.delete(id);
    }

    /**
     * 根据id查询商品图片
     * @param id
     * @return
     */
    @Cacheable(key="'productImages-one-'+ #p0")
    public ProductImage get(Integer id){
        return productImageDAO.getOne(id);
    }

    /**
     * 设置单个商品图片
     * @param product
     */
    public void setFirstProdutImage(Product product) {
        ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
        List<ProductImage> singleImages = productImageService.listSingleProductImages(product);
        if(!singleImages.isEmpty())
            product.setFirstProductImage(singleImages.get(0));
        else
            product.setFirstProductImage(new ProductImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。

    }

    /**
     * 设置多个商品图片
     * @param products
     */
    public void setFirstProdutImages(List<Product> products) {
        for (Product product : products)
            setFirstProdutImage(product);
    }

    /**
     * 设置订单列表中的商品图片
     * @param ois
     */
    public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
        for(OrderItem oi : ois){
            setFirstProdutImage(oi.getProduct());
        }
    }

}
