package com.imwj.tmall.controller.property;

import com.imwj.tmall.pojo.Property;
import com.imwj.tmall.service.PropertyService;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author langao_q
 * @create 2019-11-20 21:52
 * 属性控制类
 */
@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    /**
     * 属性集合数据
     * @param start
     * @param size
     * @param cid
     * @return
     */
    @GetMapping(value = "/properties")
    public Page4Navigator<Property> list(@RequestParam(value = "start", defaultValue = "0")int start,
                                         @RequestParam(value = "size", defaultValue = "5")int size,
                                         Integer cid){
        start = start<0?0:start;
        Page4Navigator list = propertyService.list(cid, start, size, 5);
        return list;
    }

    /**
     * 增加属性数据
     * @param property
     * @return
     */
    @PostMapping(value = "/properties")
    public Object add(@RequestBody Property property){
        Property add = propertyService.add(property);
        return add;
    }

    /**
     * 编辑回显
     * @param id
     * @return
     */
    @GetMapping(value = "/properties/{id}")
    public Property get(@PathVariable Integer id){
        return propertyService.get(id);
    }

    /**
     * 更新操作
     * @param property
     * @return
     */
    @PutMapping(value = "/properties")
    public Object update(@RequestBody Property property){
        return propertyService.update(property);
    }

    /**
     * 删除属性
     * @param id
     * @return
     */
    @DeleteMapping(value = "/properties/{id}")
    public String delete(@PathVariable Integer id){
        propertyService.delete(id);
        return null;
    }

}
