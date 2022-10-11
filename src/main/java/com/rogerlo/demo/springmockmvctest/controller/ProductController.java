package com.rogerlo.demo.springmockmvctest.controller;

import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/ProductController")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ProductController {

    private List<ProductVO> productList = Stream.of(
            ProductVO.builder().id(101).name("滑鼠").price(Double.valueOf(33.5)).build(),
            ProductVO.builder().id(102).name("鍵盤").price(Double.valueOf(55.66)).build(),
            ProductVO.builder().id(103).name("耳機").price(Double.valueOf(13)).build()
    ).collect(Collectors.toList());

    @RequestMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/getAllProducts")
    public List<ProductVO> getAllProducts() {
        return this.productList;
    }

    @DeleteMapping("/delete/{prodId}")
    public boolean delete(@PathVariable("prodId") int prodId) {
        System.err.println("delete product id: " + prodId);
        AtomicBoolean deleteFlag = new AtomicBoolean(false);
        this.productList.parallelStream().filter(pVO -> pVO.getId() == prodId)
                .findAny()
                .ifPresent(pVO -> {
                    deleteFlag.set(true);
                    this.productList.remove(pVO);
                });

        if (deleteFlag.get()) {
            System.out.println(">>> 成功刪除：" + prodId);
        } else {
            System.out.println(">>> 刪除失敗：" + prodId);
        }

        return deleteFlag.get();
    }

}
