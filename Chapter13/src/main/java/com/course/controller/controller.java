package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Log4j
@RestController
@Api(value = "v1", description = "用户管理系统")
@RequestMapping("v1")
public class controller {
    @Autowired
    private SqlSessionTemplate template;

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user) {
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        LogFactory.getLog(Controller.class).info("查询到的结果是" + i);
        if (i == 1) {
            LogFactory.getLog(Controller.class).info("登录用户是:" + user.getUsername());
            return true;
        }
        return false;

    }

    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Boolean addUser(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        int result = 0;
        if (x != null) {
            result = template.insert("addUser", user);
        }
        if (result > 0) {
            LogFactory.getLog(Controller.class).info("添加用户的数量是" + result);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "获取用户(列表)信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        if (x == true) {
            List<User> users = template.selectList("getUserInfo", user);
            LogFactory.getLog(Controller.class).info("getUserInfo获取的用户数量是" + users.size());
            return users;
        } else {
            return null;
        }

    }

    @ApiOperation(value = "更新用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(HttpServletRequest request, @RequestBody User user) {
        Boolean x = verifyCookies(request);
        int i = 0;
        if (x == true) {
            i = template.update("updateUserInfo", user);
        }
        if (i > 0) {
            LogFactory.getLog(Controller.class).info("更新数据的条数为:" + i);
            return i;
        } else {
            return 0;
        }
    }

    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            LogFactory.getLog(Controller.class).info("Cookies为空");
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                LogFactory.getLog(Controller.class).info("验证通过");
                return true;
            }
        }
        return false;
    }
}
