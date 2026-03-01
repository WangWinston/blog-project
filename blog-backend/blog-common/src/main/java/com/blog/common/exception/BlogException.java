package com.blog.common.exception;

import com.blog.common.enums.ResultCode;
import lombok.Getter;

/**
 * Blog Business Exception
 */
@Getter
public class BlogException extends RuntimeException {

    private final Integer code;
    private final String message;

    public BlogException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }

    public BlogException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BlogException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BlogException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.message = message;
    }
}