package com.cicro.vhr.entity;


@SuppressWarnings("all")
public enum ResultCode {

    SUCCESS(true,200,"操作成功！"),
    OK(true,200),
    //---系统错误返回码-----
    FAIL(false,500),
    ERROR(false,500,"操作失败"),
    UNAUTHENTICATED(false,401,"您还未登录"),
    UNAUTHORISE(false,403,"权限不足"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");

    //---用户操作返回码----
    //---企业操作返回码----
    //---权限操作返回码----
    //---其他操作返回码----

    //操作是否成功
     boolean success;
    //操作代码
     int code;
    //提示信息
     String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    ResultCode(boolean success,int code){
        this.success = success;
        this.code = code;
    }

    public  boolean success() {
        return success;
    }

    public  int code() {
        return code;
    }

    public  String message() {
        return message;
    }

}
