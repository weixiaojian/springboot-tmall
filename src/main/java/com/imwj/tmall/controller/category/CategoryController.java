package com.imwj.tmall.controller.category;

import com.imwj.tmall.pojo.Category;
import com.imwj.tmall.service.CategoryService;
import com.imwj.tmall.util.ImageUtil;
import com.imwj.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author langao_q
 * @create 2019-11-19 10:45
 * 类别控制类：只做数据返回操作
 */
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页集合数据
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0")int start,
                                         @RequestParam(value = "size", defaultValue = "5")int size)throws Exception{
        // 语法糖 防止start小于0
        start = start<0?0:start;
        // 5表示当前页面最多显示五个页码
        Page4Navigator list = categoryService.list(start, size, 5);
        return list;
    }

    /**
     * 增加类别数据
     * @param category
     * @param image
     * @param request
     * @return
     */
    @PostMapping(value = "/categories")
    public Object add(Category category, MultipartFile image, HttpServletRequest request) throws IOException {
        Category bean = categoryService.add(category);
        saveOrUpdateImageFile(bean, image, request);
        return category;
    }

    /**
     * 增加类别数据时的图片上传
     * @param bean
     * @param image
     * @param request
     */
public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)throws IOException{
    File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
    File file = new File(imageFolder, bean.getId() + ".jpg");
    if(!file.getParentFile().exists()){
        file.getParentFile().mkdir();
    }
    //transferTo：文件保存.jpg后缀名
    image.transferTo(file);
    //change2jpg：格式化成jpg文件
    BufferedImage img = ImageUtil.change2jpg(file);
    //写入磁盘
    ImageIO.write(img, "jpg", file);
}

    /**
     * 删除操作
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable Integer id, HttpServletRequest request){
        categoryService.delete(id);
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, id + ".jpg");
        file.delete();
        return null;
    }

    /**
     * 编辑回显操作
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable Integer id){
        return categoryService.get(id);
    }

    /**
     * 更新类别
     * @param category
     * @param image
     * @param request
     * @return
     * @throws IOException
     */
    @PutMapping("/categories/{id}")
    public Object update(Category category, MultipartFile image,HttpServletRequest request) throws IOException {
        String name = request.getParameter("name");
        category.setName(name);
        Category bean = categoryService.update(category);
        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return category;
    }

}
