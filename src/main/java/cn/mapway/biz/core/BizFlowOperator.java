package cn.mapway.biz.core;

import java.io.Serializable;

/**
 * 业务流结点 处理结果的后续操作
 */
public enum BizFlowOperator implements Serializable {
    BIZ_CONTINUE("continue", "继续"),
    BIZ_BREAK("break", "终止"),
    BIZ_ROLLBACK("rollback", "回滚"),
    ;

    private String code;
    private String desc;

    /**
     * @param code code
     * @param desc desc
     */
    BizFlowOperator(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @return get code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
