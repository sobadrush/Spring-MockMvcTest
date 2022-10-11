package com.rogerlo.demo.springmockmvctest.controller;

import com.rogerlo.demo.springmockmvctest.dao.ProductDao;
import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/ProductController")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @RequestMapping("/hello")
    public String hello() {
        return "hello world!";
    }

    @GetMapping("/getAllProducts")
    public List<ProductVO> getAllProducts() {
        return productDao.getAllProducts();
    }

    @GetMapping("/getProduct/{prodId}")
    public ProductVO getProduct(@PathVariable("prodId") int prodId) {
        return productDao.getAllProducts()
                .parallelStream().filter(x -> x.getId() == prodId)
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/delete/{prodId}")
    public boolean delete(@PathVariable("prodId") int prodId) {
        System.err.println("delete product id: " + prodId);
        AtomicBoolean deleteFlag = new AtomicBoolean(false);
        productDao.getAllProducts().parallelStream().filter(pVO -> pVO.getId() == prodId)
                .findAny()
                .ifPresent(pVO -> {
                    deleteFlag.set(true);
                    productDao.getAllProducts().remove(pVO);
                });

        if (deleteFlag.get()) {
            System.out.println(">>> 成功刪除：" + prodId);
        } else {
            System.out.println(">>> 刪除失敗：" + prodId);
        }

        return deleteFlag.get();
    }

}
