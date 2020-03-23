package com.imwj.tmall.service;

import com.imwj.tmall.dao.CategoryDAO;
import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-19 10:37
 * 类别service操作
 */
@Service
@CacheConfig(cacheNames="categories")
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * 查询所有的类别数据
     * @return List<Category>
     */
    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    /**
     * 分页查询数据
     * @param start 起始页
     * @param size  每页显示条数
     * @param navigatePages  导航栏显示的页码个数
     * @return
     */
    @Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
    public Page4Navigator list(int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> page = categoryDAO.findAll(pageable);
        return new Page4Navigator<>(page,navigatePages);
    }

    /**
     * 增加类别数据
     * @param category
     * @return
     */
    @CacheEvict(allEntries=true)
    public Category add(Category category){
        return categoryDAO.save(category);
    }

    /**
     * 根据id删除类别数据
     * @param id
     */
    @CacheEvict(allEntries=true)
    public void delete(Integer id){
        categoryDAO.delete(id);
    }

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @Cacheable(key="'categories-one-'+ #p0")
    public Category get(Integer id){
        return categoryDAO.getOne(id);
    }

    /**
     * 更新类别数据
     * @param category
     * @return
     */
    @CacheEvict(allEntries=true)
    public Category update(Category category) {
        return categoryDAO.save(category);
    }

    /**
     * 清除category中product中的category
     * 防止返回前台数据son序列化的时候出现递归死循环
     * @param categories
     */
    public void removeCategoryFromProduct(List<Category> categories){
        for(Category category : categories){
            removeCategoryFromProduct(category);
        }
    }
    public void removeCategoryFromProduct(Category category){
        List<Product> products = category.getProducts();
        if(null != products){
            for(Product product : products){
                product.setCategory(null);
            }
        }
    }


}
