package com.imwj.tmall.comparator;

import com.imwj.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author langao_q
 * @create 2019-12-14 11:55
 * 价格排序(高 > 低)价格
 */
public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice() - p1.getPromotePrice());
    }
}
