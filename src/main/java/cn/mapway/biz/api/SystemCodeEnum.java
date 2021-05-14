package cn.mapway.biz.api;

import cn.mapway.biz.constant.IBizCode;

/**
 * 定义的公共返回代码集
 *
 * @author zhangjs2@ziroom.com
 */
public enum SystemCodeEnum implements IBizCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "运行错误:{0}"),
    RPC_ERROR(50001000, "RPC错误{0}"),
    BIZ_EMPTY(50002000, "业务处理逻辑为空 {0}"),
    DISTRIBUTED_LOCK_ERROR(50004000, "分布式锁异常{}"),
    ERROR_TOKEN_AUTHORITY(50006000, "API请求需要添加 BIZ_TPKEN"),
    ERROR_TOKEN_INVALIDATE(50006001, "BIZ_TOKEN=%s目前不可用,联系15910868680");


    private Integer code;
    private String message;

    SystemCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     *
     * @return code
     */
    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     *
     * @return message
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}
