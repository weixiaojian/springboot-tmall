package com.imwj.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @author langao_q
 * @create 2019-11-25 16:19
 * 商品图片实体类
 */
@Data
@Entity
@Table(name="productimage")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="pid")
    @JsonBackReference
    private Product product;

    private String type;
}
