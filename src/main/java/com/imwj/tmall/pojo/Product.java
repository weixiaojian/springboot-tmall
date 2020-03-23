package com.imwj.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-24 14:19
 * 商品实体类
 */
@Data
@Entity
@Table(name="product")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Document(indexName = "tmall_springboot",type = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Category category;

    //如果既没有指明 关联到哪个Column,又没有明确要用@Transient忽略，那么就会自动关联到表对应的同名字段，默认@Column(name = "属性名")
    private String name;
    private String subTitle;
    private float originalPrice;
    private float promotePrice;
    private int stock;
    private Date createDate;

    //商品第一张图片
    @Transient
    private ProductImage firstProductImage;
    //商品简介图片集合
    @Transient
    private List<ProductImage> productSingleImages;
    ///商品详情图片集合
    @Transient
    private List<ProductImage> productDetailImages;
    //销量
    @Transient
    private int reviewCount;
    //累计评价
    @Transient
    private int saleCount;
}
