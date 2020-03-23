package com.imwj.tmall.controller.product;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.Property;
import com.imwj.tmall.service.ProductImageService;
import com.imwj.tmall.service.ProductService;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author langao_q
 * @create 2019-11-24 14:33
 * 商品控制类
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    /**
     * 商品集合数据
     * @param start
     * @param size
     * @param cid
     * @return
     */
    @GetMapping(value = "/products")
    public Page4Navigator<Property> list(@RequestParam(value = "start", defaultValue = "0")int start,
                                         @RequestParam(value = "size", defaultValue = "5")int size,
                                         Integer cid){
        start = start<0?0:start;
        Page4Navigator list = productService.list(cid, start, size, 5);
        productImageService.setFirstProdutImages(list.getContent());

        return list;
    }

    /**
     * 增加商品数据
     * @param property
     * @return
     */
    @PostMapping(value = "/products")
    public Object add(@RequestBody Product product){
        product.setCreateDate(new Date());
        Product add = productService.add(product);
        return add;
    }

    /**
     * 编辑回显
     * @param id
     * @return
     */
    @GetMapping(value = "/products/{id}")
    public Product get(@PathVariable Integer id){
        return productService.get(id);
    }

    /**
     * 更新操作
     * @param property
     * @return
     */
    @PutMapping(value = "/products")
    public Object update(@RequestBody Product product){
        return productService.update(product);
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    @DeleteMapping(value = "/products/{id}")
    public String delete(@PathVariable Integer id){
        productService.delete(id);
        return null;
    }
}
