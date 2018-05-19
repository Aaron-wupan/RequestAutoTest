package com.course.model;

import lombok.Data;

@Data
public class GetUserListCase {
    private int id;
    private String username;
    private int age;
    private String sex;
    private String permission;
    private String isDelete;
    private String expected;
}
