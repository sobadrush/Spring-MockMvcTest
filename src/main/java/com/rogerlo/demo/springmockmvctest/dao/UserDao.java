package com.rogerlo.demo.springmockmvctest.dao;


import com.rogerlo.demo.springmockmvctest.model.UserVO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class UserDao {

    private List<UserVO> userList = Stream.of(
            UserVO.builder().userId("72782fef-1199-4204-9569-f45ec3990d61").username("普丁").password("qqq").build(),
            UserVO.builder().userId("9576655d-6c9a-4eae-8b54-e6e7a5895fe6").username("科比").password("www").build(),
            UserVO.builder().userId("b36bfb3e-fecb-4943-966e-eed8b4926e90").username("普爾").password("eee").build()
    ).collect(Collectors.toList());

    /**
     * 根據 user id 取得 userVO
     */
    public UserVO getUserById(String uid) {
        System.out.println(" >>> 「UserDao」getUserById was run.");
        return this.userList.parallelStream().filter(vo -> vo.getUserId().equals(uid)).findFirst().get();
    }

}
