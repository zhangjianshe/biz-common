package cn.mapway.biz.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务处理单元 请求数据结构
 *
 * @param <T> type
 */
@Data
public class BizRequest<T> implements Serializable {
    /**
     * 业务类型
     */
    String bizType;

    T data;

    /**
     * 包装请求数据到业务处理逻辑中
     *
     * @param bizType bizType
     * @param data data
     * @param <T>  T
     * @return return data
     */
    public static <T> BizRequest<T> wrap(String bizType, T data) {
        BizRequest<T> result = new BizRequest<>();
        result.setBizType(bizType);
        result.setData(data);
        return result;
    }
}
