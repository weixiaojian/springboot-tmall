package com.imwj.tmall.dao;

import com.imwj.tmall.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author langao_q
 * @create 2019-11-19 10:36
 * 类别dao操作
 */
public interface CategoryDAO extends JpaRepository<Category, Integer> {


}
