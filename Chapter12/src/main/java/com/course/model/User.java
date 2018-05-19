package com.course.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private int age;
    private String sex;
    private String permission;
    private String isDelete;

    public String toString() {
        return (
                "{id:" + id + "," +
                        "username:" + username + "," +
                        "password:" + password + "," +
                        "age:" + age + "," +
                        "sex:" + sex + "," +
                        "permission:" + permission + "," +
                        "isDelete:" + isDelete + "}"
        );
    }
}
