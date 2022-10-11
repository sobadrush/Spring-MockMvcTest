package com.rogerlo.demo.springmockmvctest.dao;


import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ProductDao {

    // 假資料
    private List<ProductVO> allProducts = Stream.of(
            ProductVO.builder().id(101).name("滑鼠").price(Double.valueOf(33.5)).build(),
            ProductVO.builder().id(102).name("鍵盤").price(Double.valueOf(55.66)).build(),
            ProductVO.builder().id(103).name("耳機").price(Double.valueOf(13)).build()
    ).collect(Collectors.toList());

    public List<ProductVO> getAllProducts() {
        return this.allProducts;
    }

}
