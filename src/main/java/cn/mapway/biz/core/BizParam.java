package cn.mapway.biz.core;

import java.io.Serializable;

/**
 * 参与业务流节点计算的基类
 */
public class BizParam implements Serializable {
    /**
     * 业务身份
     */
    private String bizType;

    /**
     *
     * @return string
     */
    public String getBizType() {
        return bizType;
    }

    /**
     *
     * @param bizType set bizType
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}
