package com.example.demo.result;

import com.sun.org.apache.bcel.internal.classfile.Code;
import org.thymeleaf.expression.Objects;

public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS=new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR=new CodeMsg(500100,"service ERROR");
    public static CodeMsg BIND_ERROR=new CodeMsg(500101,"参数校验异常:%s");
    //登录模块 5002XX
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500211,"手机号不能为空");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500212,"密码不能为空");
    public static CodeMsg USER_NOTEXIST=new CodeMsg(500213,"用户不存在");
    public static CodeMsg PASSWORD_ERROR=new CodeMsg(500214,"密码错误");
    public static CodeMsg User_HASEXIST=new CodeMsg(500215,"用户已经存在");
    //商品模块 5003XX
    public static CodeMsg SESSION_ERROR=new CodeMsg(500300,"缓存失效");
    //订单模块 5004XX
    public static CodeMsg Stock_NotEnough=new CodeMsg(500400,"库存不足");
    public static CodeMsg ORDER_NOTEXIST=new CodeMsg(500401,"订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg MiaoSha_HasFinished=new CodeMsg(500500,"已经秒杀过了");
    public static CodeMsg MiaoSha_URLERROR=new CodeMsg(500501,"秒杀地址错误");
    public static CodeMsg MiaoSha_Failed=new CodeMsg(500502,"秒杀失败");
    public static CodeMsg MiaoSha_VerifyFailed=new CodeMsg(500503,"验证码错误");
    public static CodeMsg MiaoSha_AccessCount=new CodeMsg(500504,"访问过于频繁");
     private CodeMsg(int code,String msg){
        this.code=code;
        this.msg=msg;
    }
    //带参数的msg
    public CodeMsg fillArgs(String args){
        int code=this.code;
        //将接收到的参数与异常拼接
        String message=String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    public int getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


}
