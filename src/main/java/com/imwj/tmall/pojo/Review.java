package com.imwj.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author langao_q
 * @create 2019-12-06 15:11
 * 评价类
 */
@Data
@Entity
@Table(name = "")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    @ManyToOne
    @JoinColumn(name="pid")
    private Product product;

    @Column(name = "createDate")
    private Date createDate;
}
