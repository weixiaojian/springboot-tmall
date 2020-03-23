package com.imwj.tmall.es;

import com.imwj.tmall.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author langao_q
 * @create 2020-03-23 10:53
 */
public interface ProductESDAO extends ElasticsearchRepository<Product, Integer> {

}
