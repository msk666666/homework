package com.example.demo.util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String pass){
        return DigestUtils.md5Hex(pass);
    }
    private static final String salt="1a2b3c4d";
    //对输入表单输入的密码加密一次
    public static String inputPassFormPass(String inputPass){
        String pass=""+salt.charAt(0)+salt.charAt(3)+inputPass+salt.charAt(6)+salt.charAt(7);
        String password = md5(pass);
        return password;
    }
//f9d385ffe6e4123902cb36c1c0c023a4
    //对服务端得到的密码 从服务器到数据库在进行一次加密  需要生成随机的盐
    public static String formPassToDBPass(String formPass,String salt){
        String pass=""+salt.charAt(0)+salt.charAt(3)+formPass+salt.charAt(6)+salt.charAt(7);
        String password = md5(pass);
        return password;
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String formPass = inputPassFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));

    }
}
