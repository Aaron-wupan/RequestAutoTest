package com.course.utils;

import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

    public static String getUrl(InterfaceName name) {
        String address = bundle.getString("test.url");
        String uri = "";
        //最终测试地址
        String testUrl;

        if (name == InterfaceName.GETUSERLIST) {
            uri = bundle.getString("getUserList.url");
        }
        if (name == InterfaceName.GETUSERINFO) {
            uri = bundle.getString("getUserInfo.url");
        }
        if (name == InterfaceName.UPDATEUSERINFO) {
            uri = bundle.getString("updateUserInfo.url");
        }
        if (name == InterfaceName.LOGIN) {
            uri = bundle.getString("login.url");
        }
        if (name == InterfaceName.ADDUSER) {
            uri = bundle.getString("addUser.url");
        }
        testUrl = address + uri;
        return testUrl;

    }

}
