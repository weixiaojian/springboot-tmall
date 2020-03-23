package com.imwj.tmall.service;

import com.imwj.tmall.dao.PropertyValueDAO;
import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.Property;
import com.imwj.tmall.pojo.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author langao_q
 * @create 2019-12-02 14:39
 * 属性值service类
 */
@Service
@CacheConfig(cacheNames="propertyValues")
public class PropertyValueService {

    @Autowired
    private PropertyValueDAO propertyValueDAO;

    @Autowired
    private PropertyService propertyService;

    /**
     * 更新属性值
     * @param bean
     */
    @CacheEvict(allEntries=true)
    public void update(PropertyValue bean) {
        propertyValueDAO.save(bean);
    }

    /**
     * 初始化属性值
     * @param product
     */
    public void init (Product product){
        List<Property> properties = propertyService.listByCategory(product.getCategory());
        for (Property property : properties){
            PropertyValue propertyValue = getByPropertyAndProduct(property, product);
            if(propertyValue == null){
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    /**
     * 根据属性和商品获取指定属性值
     * @param property
     * @param product
     * @return
     */
    @Cacheable(key="'propertyValues-one-ptid-'+#p0.id+ '-pid-' + #p1.id")
    public PropertyValue getByPropertyAndProduct(Property property, Product product){
        return propertyValueDAO.getByPropertyAndProduct(property, product);
    }

    /**
     * 根据商品获取属性值列表
     * @param product
     * @return
     */
    @Cacheable(key="'propertyValues-one-pid-' + #p0.id")
    public List<PropertyValue> list(Product product){
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }

}
