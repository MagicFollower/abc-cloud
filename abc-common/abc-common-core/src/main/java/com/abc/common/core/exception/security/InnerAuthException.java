package com.abc.common.core.exception.security;

/**
 * 内部认证异常
 * 
 * @author abc
 */
public class InnerAuthException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message)
    {
        super(message);
    }
}
