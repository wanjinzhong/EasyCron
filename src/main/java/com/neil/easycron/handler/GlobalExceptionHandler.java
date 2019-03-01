package com.neil.easycron.handler;


import javax.servlet.http.HttpServletRequest;

import com.neil.easycron.bo.response.JsonEntity;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕捉shiro的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public JsonEntity handle401(ShiroException e) {
        logger.error("ERROR: " + e.getMessage(), e);
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(401);
        jsonEntity.setMessage(e.getMessage());
        return jsonEntity;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnknownAccountException.class)
    public JsonEntity handle401(UnknownAccountException e) {
        logger.error("ERROR: " + e.getMessage(), e);
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(401);
        jsonEntity.setMessage("账号或密码错误");
        return jsonEntity;
    }

    /**
     * 捕捉UnauthorizedException
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public JsonEntity handle401() {
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(403);
        jsonEntity.setMessage("未授权");
        return jsonEntity;
    }

    /**
     * 捕捉AccountException
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccountException.class)
    public JsonEntity handleAccountException(AccountException e) {
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(401);
        jsonEntity.setMessage(e.getMessage());
        return jsonEntity;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public JsonEntity handleUnauthenticatedException() {
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(401);
        jsonEntity.setMessage("未登陆");
        return jsonEntity;
    }

    /**
     * 捕捉AccountException
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonEntity globalException(HttpServletRequest request, Throwable e) {
        logger.error("ERROR: " + e.getMessage(), e);
        // report(e);
        JsonEntity jsonEntity = new JsonEntity();
        jsonEntity.setCode(getStatus(request).value());
        jsonEntity.setMessage(e.getMessage());
        return jsonEntity;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
