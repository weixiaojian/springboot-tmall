package com.imwj.tmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author langao_q
 * @create 2019-11-19 10:41
 * 后台管理页面跳转专用控制器：只做页面跳转
 */
@Controller
public class AdminPageController {

    /**
     * 首页请求：转发到分类管理页面
     * @return
     */
    @GetMapping(value = "/admin")
    public String admin(){
        return "redirect:admin_category_list";
    }

    /**
     * 分类管理页面
     * @return
     */
    @GetMapping(value = "/admin_category_list")
    public String admin_lit(){
        return "/admin/listCategory";
    }

    /**
     * 分类修改页面
     * @return
     */
    @GetMapping(value = "/admin_category_edit")
    public String editCategory(){
        return "/admin/editCategory";
    }

    /**
     * 属性管理页面
     * @return
     */
    @GetMapping(value = "/admin_property_list")
    public String property_lit(){
        return "/admin/listProperty";
    }

    /**
     * 属性修改页面
     * @return
     */
    @GetMapping(value = "/admin_property_edit")
    public String editProperty(){
        return "/admin/editProperty";
    }

    /**
     * 商品管理页面
     * @return
     */
    @GetMapping(value = "/admin_product_list")
    public String product_list(){
        return "/admin/listProduct";
    }

    /**
     * 商品修改页面
     * @return
     */
    @GetMapping(value = "/admin_product_edit")
    public String editPqroduct(){
        return "/admin/editProduct";
    }

    /**
     * 商品图片页面
     * @return
     */
    @GetMapping(value = "/admin_productImage_list")
    public String product_list_img(){
        return "/admin/listProductImage";
    }

    /**
     * 属性值页面
     * @return
     */
    @GetMapping(value = "/admin_propertyValue_edit")
    public String propertyValue_edit(){
        return "/admin/editPropertyValue";
    }

    /**
     * 用户管理页面
     * @return
     */
    @GetMapping(value = "/admin_user_list")
    public String user_list(){
        return "/admin/listUser";
    }

    /**
     * 订单管理页面
     * @return
     */
    @GetMapping(value = "/admin_order_list")
    public String order_list(){
        return "/admin/listOrder";
    }

}
