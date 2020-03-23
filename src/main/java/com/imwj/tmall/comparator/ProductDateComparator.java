package com.imwj.tmall.comparator;

import com.imwj.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author langao_q
 * @create 2019-12-14 11:55
 * 新品排序(高 > 低)上架日期
 */
public class ProductDateComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getCreateDate().compareTo(p1.getCreateDate());
    }
}
