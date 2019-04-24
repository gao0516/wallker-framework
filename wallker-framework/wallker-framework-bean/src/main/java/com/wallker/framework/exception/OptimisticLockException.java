package com.wallker.framework.exception;

/**
 * 乐观锁
 * @Filename: OptimisticLockException.java
 * @Description:
 * @Author: Wallker.Gao
 * @Date: 2019年01月10日
 * @Version: 1.0
 * @Email: gao0516.jian@163.com
 */
@SuppressWarnings("serial")
public class OptimisticLockException extends BaseException {

	public OptimisticLockException(String errorMsg) {
		super("",errorMsg);
	}

	public OptimisticLockException(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

	public OptimisticLockException(String errorCode, Throwable caused) {
		super(errorCode, caused);
	}

	public OptimisticLockException(String errorCode, String errorMsg, Throwable caused) {
		super(errorCode, errorMsg, caused);
	}
}
