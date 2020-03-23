package com.imwj.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author langao_q
 * @create 2019-11-19 10:30
 * 类别实体类
 */
@Data
@Entity
@Table(name="category")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    //如果既没有指明 关联到哪个Column,又没有明确要用@Transient忽略，那么就会自动关联到表对应的同名字段，默认@Column(name = "属性名")
    private String name;

    //一个分类下有多个产品
    @Transient
    List<Product> products;

    //一个分类又对应多个 List<Product>
    @Transient
    List<List<Product>> productsByRow;
}
