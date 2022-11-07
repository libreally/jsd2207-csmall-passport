package cn.tedu.csmall.passport.ex.handler;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 全局异常处理器
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
        log.debug("创建全局异常处理器对象：GlobalExceptionHandler");
    }

    @ExceptionHandler
    public JsonResult<Void> handleServiceException(ServiceException e) {
        log.debug("开始处理ServiceException");
        return JsonResult.fail(e);
    }

    @ExceptionHandler
    public JsonResult<Void> handleBindException(BindException e) {
        log.debug("开始处理BindException");

        String defaultMessage = e.getFieldError().getDefaultMessage();
        //StringJoiner stringJoiner = new StringJoiner("，", "请求参数格式错误，", "！！！");
        //List<FieldError> fieldErrors = e.getFieldErrors();
        //for (FieldError fieldError : fieldErrors) {
        //    String defaultMessage = fieldError.getDefaultMessage();
        //    stringJoiner.add(defaultMessage);
        //}

        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, defaultMessage);
    }

    @ExceptionHandler
    public JsonResult<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.debug("开始处理ConstraintViolationException");
        StringJoiner stringJoiner = new StringJoiner("，");
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            stringJoiner.add(constraintViolation.getMessage());
        }
        return JsonResult.fail(ServiceCode.ERR_BAD_REQUEST, stringJoiner.toString());
    }

    @ExceptionHandler({
            InternalAuthenticationServiceException.class, // AuthenticationServiceException >>> AuthenticationException
            BadCredentialsException.class // AuthenticationException
    })
    public JsonResult<Void> handleAuthenticationException(AuthenticationException e) {
        log.debug("捕获到AuthenticationException");
        log.debug("异常类型：{}", e.getClass().getName());
        log.debug("异常消息：{}", e.getMessage());
        String message = "登录失败，用户名或密码错！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED, message);
    }

    @ExceptionHandler
    public JsonResult<Void> handleDisabledException(DisabledException e) {
        log.debug("捕获到DisabledException");
        String message = "登录失败，此账号已经被禁用！";
        return JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED_DISABLED, message);
    }


    @ExceptionHandler
    public String handleThrowable(Throwable e){
        String message="你有异常未被处理,根据服务器控制台信息补充异常!!!";
        e.printStackTrace();
        return message;
    }
}
