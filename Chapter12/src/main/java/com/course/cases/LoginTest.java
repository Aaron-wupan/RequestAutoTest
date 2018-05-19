package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.LoginCase;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {
//    @Test(description = "登录接口")
//    public void loginTest() throws IOException {
//        SqlSession session= DatabaseUtil.getSqlSession();
//        LoginCase loginCase=session.selectOne("loginCase",1);
//        System.out.println(loginCase.toString());
//        System.out.println(TestConfig.loginUrl);
//    }

    @BeforeTest(groups = "loginTrue", description = "测试准备工作,获取HTTPClient对象")
    public void BeforeTest() {
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSER);

        TestConfig.defaultHttpClient = new DefaultHttpClient();

    }


    @Test(groups = "loginTrue", description = "用户登录成功接口测试")
    protected void loginTrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", 1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //第一步:发送请求
        String result = getResult(loginCase);

        //第二步:验证结果
        Assert.assertEquals(loginCase.getExpected(), result);

    }


    @Test(groups = "loginFalse", description = "用户登录失败接口测试")
    protected void loginFalse() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase", 1);
        Thread.sleep(1000);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //第一步:发送请求,获取结果
        String result = getResult(loginCase);

        //第二步:验证结果
        Assert.assertEquals(loginCase.getExpected(), result);

    }

    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("username", loginCase.getUsername());
        param.put("password", loginCase.getPassword());

        post.setHeader("content-type", "application/json");

        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        return result;
    }
}
