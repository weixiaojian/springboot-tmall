package com.imwj.tmall.comparator;

import com.imwj.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author langao_q
 * @create 2019-12-14 11:55
 * 综合排序(高 > 低)销量*评价
 */
public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()*p2.getSaleCount() - p1.getReviewCount()*p1.getSaleCount();
    }
}
