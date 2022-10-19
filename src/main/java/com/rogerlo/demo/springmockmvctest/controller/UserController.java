package com.rogerlo.demo.springmockmvctest.controller;

import com.rogerlo.demo.springmockmvctest.dao.UserDao;
import com.rogerlo.demo.springmockmvctest.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/UserController")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/getSessionUser/{userUUID}")
    public UserVO getSessionUser(@PathVariable String userUUID, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("「getSessionUser」Session isNew: " + session.isNew());
        System.out.println("「getSessionUser」 userUUID = " + userUUID);
        UserVO userVO = (UserVO) session.getAttribute(userUUID);
        return userVO != null ? userVO : null;
    }

    @PostMapping("/setUserIntoSession")
    public ResponseEntity setSessionUser(@RequestParam String username, @RequestParam String password,
                                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("「setSessionUser」Session isNew: " + session.isNew());
        UserVO userVO = new UserVO(username, password);
        String userId = userVO.getUserId();
        System.out.println(MessageFormat.format("「setSessionUser」UserId: {0}, UserVO: {1}", userId, userVO));
        session.setAttribute(userId, userVO);
        return Objects.nonNull(session.getAttribute(userId)) ?
                new ResponseEntity<>(userId, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getUserById/{userUUID}")
    public UserVO getUserById(@PathVariable String userUUID) {
        return userDao.getUserById(userUUID);
    }

    /**
     * 密碼隱碼
     */
    public String maskPassword(String password) {
        System.out.println(" >>> maskPassword was run.");
        StringBuilder sb = new StringBuilder();
        char[] pswdChars = password.toCharArray();
        IntStream.range(0, pswdChars.length)
            .forEach(idx -> {
                if (idx == 0 || idx == (pswdChars.length - 1)) {
                    sb.append(pswdChars[idx]);
                } else {
                    sb.append("*");
                }
            });
        return sb.toString();
    }

}
