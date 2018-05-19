package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserListCase;
import com.course.model.User;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetUserInfoListTest {
    @Test(dependsOnGroups = "loginTrue", description = "获取用户列表接口")
    public void getUserInfoListTest() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase", 1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);

        //发送请求获取结果(返回结果为JSON格式)
        JSONArray resultJson = getJsonRequest(getUserListCase);

        //验证返回结果
        List<User> userList = session.selectList(getUserListCase.getExpected(), getUserListCase);
//        List<User> userList = session.selectList();
        for (User user : userList) {
            System.out.println("获取的用户为:" + user.getUsername());
        }
        JSONArray userListJson = new JSONArray(userList);
        //将获取的用户数(JSON长度)与用户数比较
        Assert.assertEquals(userListJson.length(), resultJson.length());

        //比较用户信息是否相同
        for (int i = 0; i < resultJson.length(); i++) {
            JSONObject expect = (JSONObject) resultJson.get(i);
            JSONObject actual = (JSONObject) userListJson.get(i);
            Assert.assertEquals(expect.toString(), actual.toString());
        }

    }

    private JSONArray getJsonRequest(GetUserListCase getUserListCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        JSONObject param = new JSONObject();
        param.put("username", getUserListCase.getUsername());
        param.put("sex", getUserListCase.getSex());
        param.put("age", getUserListCase.getAge());
        post.setHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        TestConfig.defaultHttpClient.setCookieStore(TestConfig.store);

        String result;
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);

        result = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONArray jsonArray = new JSONArray(result);
        return jsonArray;
    }
}
