package com.imwj.tmall.service;

import com.imwj.tmall.dao.CategoryDAO;
import com.imwj.tmall.dao.ProductDAO;
import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.util.Page4Navigator;
import com.imwj.tmall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-24 14:25
 * 商品service类
 */
@Service
@CacheConfig(cacheNames="products")
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReviewService reviewService;

    /**
     * 分页查询数据
     * @param start 起始页
     * @param size  每页显示条数
     * @param navigatePages  导航栏显示的页码个数
     * @return
     */
    @Cacheable(key="'products-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public Page4Navigator <Product> list(int cid, int start, int size, int navigatePages){
        Category category = categoryDAO.getOne(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> page = productDAO.findByCategory(category, pageable);
        return new Page4Navigator<>(page, navigatePages);
    }

    /**
     * 增加商品数据
     * @param product
     * @return
     */
    @CacheEvict(allEntries=true)
    public Product add(Product product){
        return productDAO.save(product);
    }

    /**
     * 根据id删除商品数据
     * @param id
     */
    @CacheEvict(allEntries=true)
    public void delete(Integer id){
        productDAO.delete(id);
    }

    /**
     * 根据id查询商品数据
     * @param id
     * @return
     */
    @Cacheable(key="'products-one-'+ #p0")
    public Product get(Integer id){
        return productDAO.getOne(id);
    }

    /**
     * 更新商品数据
     * @param product
     * @return
     */
    @CacheEvict(allEntries=true)
    public Product update(Product product){
        return productDAO.save(product);
    }

    /**
     * 给分类集合填充商品数据
     * @param categories
     */
    public void fill(List<Category> categories){
        for(Category category : categories)
            fill(category);
    }

    /**
     * 给单个分类填充商品数据
     * listByCategory 方法本来就是 ProductService 的方法，却不能直接调用
     * springboot 的缓存机制是通过切面编程 aop来实现的
     * 从fill方法里直接调用 listByCategory 方法， aop 是拦截不到的，也就不会走缓存了
     * @param category
     */
    public void fill(Category category){
        ProductService productService = SpringContextUtil.getBean(ProductService.class);
        List<Product> products = productService.listByCategory(category);
        productImageService.setFirstProdutImages(products);
        category.setProducts(products);
    }

    /**
     * 为多个分类填充推荐产品集合，即把分类下的产品集合，
     * 按照8个为一行，拆成多行，以利于后续页面上进行显示
     * @param categories
     */
    public void fillByRow(List<Category> categories){
        int productNumberEachRow = 8;
        for(Category category : categories){
            List<Product> products = category.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();
            for(int i=0; i<products.size(); i+=productNumberEachRow){
                int size = i + productNumberEachRow;
                size = size>products.size()?products.size():size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productsByRow);
        }
    }

    /**
     * 根据分类查询商品集合数据
     * @param category
     * @return
     */
    @Cacheable(key="'products-cid-'+ #p0.id")
    public List<Product> listByCategory(Category category){
        return productDAO.findByCategoryOrderById(category);
    }

    /**
     * 给商品集合设置销售总数和评论总数
     * @param products
     */
    public void setSaleAndReviewNumber(List<Product> products){
        for (Product product : products)
            setSaleAndReviewNumber(product);
    }
    /**
     * 给商品集合设置销售总数和评论总数
     * @param products
     */
    public void setSaleAndReviewNumber(Product product){
        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    /**
     * 首页搜索
     * @param keyword
     * @param i
     * @param i1
     * @return
     */
    public List<Product> searh(String keyword, int star, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(star, size, sort);
        List<Product> products = productDAO.findByNameLike("%" + keyword + "%", pageable);
        return products;
    }
}
