package com.wallker.framework.exception;

/**
 * 登录已过期异常
 * @Filename: LoginExpireException.java
 * @Description: 
 * @Author: Wallker.Gao
 * @Date: 2019年01月10日
 * @Version: 1.0
 * @Email: gao0516.jian@163.com
 */
public class LoginExpireException extends Exception {

    private static final long serialVersionUID = 1L;

    public LoginExpireException() {
        super();
    }

    public LoginExpireException(String message) {
        super(message);
    }

    public LoginExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginExpireException(Throwable cause) {
        super(cause);
    }

    protected LoginExpireException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static boolean is(Exception error) {
        return error instanceof LoginExpireException;
    }

}
