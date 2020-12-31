package com.cicro.vhr.controller.exception;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/*
 * @className: GlobalExceptionHandler
 * @description 全局异常处理
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/7/23 0023 
 * @version 1.0.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 系统异常 预期以外异常
     * @param ex
     * @return
     */
   @ExceptionHandler(SQLException.class)
   @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
   public Result handleUnexpectedServer(SQLException ex) {
       Result result = new Result(ResultCode.FAIL);
       result.setMessage(ex.getMessage());
       return result;
   }
/*
    *//**
     * 缺少请求参数异常
     * @param ex
     * @return
     *//*
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnResult handleMissingNotRequestParameterException(Exception ex) {
        return new ReturnResult(BusinessMsgEnum.PARMETER_EXCEPTION.getCode(),
            BusinessMsgEnum.PARMETER_EXCEPTION.getMsg());
    }

    *//**
     * 数学异常
     * @param ex
     * @return
     *//*
    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ReturnResult handleArithmeticException(Exception ex) {
        return new ReturnResult("9999",
            "数学异常");
    }

    *//**
     * 空指针异常
     * @param ex NullPointerException
     * @return
     *//*
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ReturnResult handleTypeMismatchException(NullPointerException ex) {
        log.error("空指针异常，{}", ex.getMessage());
        return new ReturnResult("500", "空指针异常了");
    }

    *//**
     * 处理Shiro权限拦截异常
     * @return
     *//*
    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ReturnResult defaultErrorHandler(AuthorizationException ex){
        log.info("权限不足{}", ex.getMessage());
        return new ReturnResult("403","权限不足");
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ReturnResult handleException(Exception ex) {
        log.error("系统异常，{}", ex.getMessage());
        return new ReturnResult("500", "空指针异常了");
    }

    *//**
     * 全局数据绑定 定义完成后，在任何一个Controller 的接口中，都可以获取到这里定义的数据
     * @return
     *//*
    @ModelAttribute(name = "md")
    public List mydata(){
       List list=new ArrayList();
       list.add("1");
       list.add(2);
       return list;
    }*/

}
