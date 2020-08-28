package com.cp.tencentlivesimple.login.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String phone;
    public static String userId = "1234567";
    public String userSig;
    public String userName;
    public String userAvatar;

    @java.lang.Override
    public java.lang.String toString() {
        return "UserModel{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                ", userSig='" + userSig + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
