package io.github.fantasticname.dormitory.repair.exception;

/**
 * 业务异常类
 * 
 * @author FantasticName
 */
public class BusinessException extends RuntimeException {

    private int code;

    /**
     * 构造方法
     * 
     * @param code 错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法
     * 
     * @param message 错误信息
     */
    public BusinessException(String message) {
        this(400, message);
    }

    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置错误码
     * 
     * @param code 错误码
     */
    public void setCode(int code) {
        this.code = code;
    }

}