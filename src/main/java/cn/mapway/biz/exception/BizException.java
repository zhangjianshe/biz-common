package cn.mapway.biz.exception;

import cn.mapway.biz.api.SystemCodeEnum;
import cn.mapway.biz.constant.BizCode;
import cn.mapway.biz.constant.IBizCode;

/**
 * Biz系统的异常定义
 *
 * @author zhangjianshe
 */
public class BizException extends RuntimeException {
    private IBizCode response = null;
    private String[] objects;

    /**
     *
     * @param response response
     * @param objs objs
     */
    public BizException(IBizCode response, String... objs) {
        super("", null, true, false);
        this.response = response;
        this.objects = objs;
    }

    /**
     * 构造异常类
     *
     * @param resultCode resultCode
     * @param objs objs
     * @return data
     */
    public static BizException get(IBizCode resultCode, String... objs) {
        BizException designException = new BizException(resultCode, objs);
        return designException;
    }

    /**
     * 构造异常类
     *
     * @param message message
     * @return data
     */
    public static BizException get(final String message) {
        BizException designException = new BizException(SystemCodeEnum.FAIL, message);
        return designException;
    }

    /**
     * 构造异常类
     *
     * @param code code
     * @param message messages
     * @return data
     */
    public static BizException get(final Integer code, final String message) {
        IBizCode bizCode = new BizCode(code, message);
        BizException designException = new BizException(bizCode, null);
        return designException;
    }

    
    /** 
     * @param code code
     * @param message message
     */
    public static void throwException(final Integer code, final String message) {
        throw get(code, message);
    }

    
    /** 
     * @param message message
     */
    public static void throwException(final String message) {
        throw get(message);
    }

    
    /** 
     * @param resultCode resultCode
     * @param objs objs
     */
    public static void throwException(IBizCode resultCode, String... objs) {
        throw get(resultCode, objs);
    }

    
    /** 
     * @return IBizCode
     */
    public IBizCode getResponse() {

        return response.bind(objects);
    }

    
    /** 
     * @return String
     */
    public String getMessage() {
        if (response != null) {
            return response.getMessage();
        }
        return "";
    }

    
    /** 
     * @return Integer
     */
    public Integer getCode() {
        return response != null ? response.getCode() : SystemCodeEnum.FAIL.getCode();
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {

        if (getResponse() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("{ 'code':").append(getResponse().getCode())
                    .append(",'message':\"").append(getResponse().bind(objects))
                    .append("\"}");
            return sb.toString();
        }
        return "{code:-1,message:'unknown message'}";
    }
}
