package com.imwj.tmall.service;

import com.imwj.tmall.dao.CategoryDAO;
import com.imwj.tmall.dao.PropertyDAO;
import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.pojo.Property;
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
 * @create 2019-11-20 21:44
 * 属性service类
 */
@Service
@CacheConfig(cacheNames = "properties")
public class PropertyService {

    @Autowired
    private PropertyDAO propertyDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * 分页查询数据
     * @param start 起始页
     * @param size  每页显示条数
     * @param navigatePages  导航栏显示的页码个数
     * @return
     */
    @Cacheable(key="'properties-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public Page4Navigator list(int cid, int start, int size, int navigatePages){
        Category category = categoryDAO.getOne(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Property> page = propertyDAO.findByCategory(category, pageable);
        return new Page4Navigator<>(page,navigatePages);
    }

    /**
     * 增加类别数据
     * @param property
     * @return
     */
    @CacheEvict(allEntries=true)
    public Property add(Property property){
        return propertyDAO.save(property);
    }

    /**
     * 根据id删除类别数据
     * @param id
     */
    @CacheEvict(allEntries=true)
    public void delete(Integer id){
        propertyDAO.delete(id);
    }

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @Cacheable(key="'properties-one-'+ #p0")
    public Property get(Integer id){
        return propertyDAO.getOne(id);
    }

    /**
     * 更新类别数据
     * @param Property
     * @return
     */
    @CacheEvict(allEntries=true)
    public Property update(Property Property) {
        return propertyDAO.save(Property);
    }

    /**
     * 根据分类获取所有属性
     * @param category
     * @return
     */
    @Cacheable(key="'properties-cid-'+ #p0.id")
    public List<Property> listByCategory(Category category){
        return propertyDAO.findByCategory(category);
    }
}
