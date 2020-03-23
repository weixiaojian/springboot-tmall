package com.imwj.tmall.controller.productImage;

import com.imwj.tmall.pojo.Product;
import com.imwj.tmall.pojo.ProductImage;
import com.imwj.tmall.service.ProductImageService;
import com.imwj.tmall.service.ProductService;
import com.imwj.tmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-26 16:26
 * 商品图片控制类
 */
@RestController
public class ProductImageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;


    /**
     * 商品图片集合数据
     * @param type
     * @param pid
     * @return
     */
    @GetMapping(value = "/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid){
        Product product = productService.get(pid);
        if(ProductImageService.type_detail.equals(type)){
            return productImageService.listDetailProductImages(product);
        }else if(ProductImageService.type_single.equals(type)){
            return productImageService.listSingleProductImages(product);
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * 添加商品图篇
     * @param pid
     * @param type
     * @param image
     * @param request
     * @return
     */
    @PostMapping(value = "/productImages")
    public ProductImage add(@RequestParam("pid") int pid, @RequestParam("type") String type, MultipartFile image
            , HttpServletRequest request)throws Exception{
        //查询商品数据，然后保存ProductImage对象
        ProductImage productImage = new ProductImage();
        Product product = productService.get(pid);
        productImage.setProduct(product);
        productImage.setType(type);
        productImageService.add(productImage);

        //计算新文件保存地址：缩略图和详细图分开保存
        String  folder = "img/";
        if(ProductImageService.type_single.equals(productImage.getType())){
            folder += "productSingle";
        }else{
            folder += "productDetail";
        }
        //新文件地址
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, productImage.getId() + ".jpg");
        //新文件地址的文件夹不存在就创建
        String fileName = file.getName();
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        //复制文件，然后写入磁盘
        try {
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }catch (IOException e){
            e.printStackTrace();
        }
        //如果是缩略图的话还要备份两份文件小图和中间图
        if(ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File file_small = new File(imageFolder_small, fileName);
            File file_middle = new File(imageFolder_middle, fileName);
            ImageUtil.resizeImage(file, 56, 56, file_small);
            ImageUtil.resizeImage(file, 217, 190, file_middle);
        }
        return productImage;
    }

    /**
     * 删除商品图片
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping(value = "/productImages/{id}")
    public String delete(@PathVariable Integer id, HttpServletRequest request){
        //查询productImage数据
        ProductImage productImage = productImageService.get(id);
        productImageService.delete(id);
        //找到文件路径
        String  folder = "img/";
        if(ProductImageService.type_single.equals(productImage.getType())){
            folder += "productSingle";
        }else{
            folder += "productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, productImage.getId() + ".jpg");
        //拿到文件名称 并删除改文件
        String fileName = file.getName();
        file.delete();
        //如果是缩略图还要删除其他两个备份文件
        if(ProductImageService.type_single.equals(productImage.getType())){
            String imageFolder_small = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getServletContext().getRealPath("img/productSingle_middle");
            File file_small = new File(imageFolder_small, fileName);
            File file_middle = new File(imageFolder_middle, fileName);
            file_small.delete();
            file_middle.delete();
        }
        return null;
    }
}
