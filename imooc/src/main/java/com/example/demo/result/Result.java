package com.example.demo.result;

import com.sun.org.apache.bcel.internal.classfile.Code;

public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private int status;

    /***
     *成功的时候调用
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /*
    失败时候调用
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    /**
     * 响应的状态
     *
     */
    public static  Result status(int status){
        return new Result(status);
    }

    public Result(int status) {
        this.status = status;
    }

    private Result(CodeMsg cm){
        if(cm==null){
            return;
        }
        this.code=cm.getCode();
        this.msg=cm.getMsg();
    }

    private Result(T data){
        this.code=0;
        this.msg="success";
        this.data=data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
