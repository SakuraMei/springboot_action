package com.sakura.xdvideo.exception;


/**
 * 自定义异常类
 *
 * @author sakura
 */
public class XdException extends RuntimeException {


    /**
     * 状态码
     */
    private Integer code;
    /**
     * 异常消息
     */
    private String msg;


    public XdException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
