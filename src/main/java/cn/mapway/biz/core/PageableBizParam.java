package cn.mapway.biz.core;

import lombok.Data;

/**
 * 业务参数 增加用于分页查询的条件
 *
 * @author zhangjianshe
 */
@Data
public class PageableBizParam extends BizParam {
    /**
     * 查询起始页
     */
    public Integer current = 1;

    /**
     * 查询页大小
     */
    public Integer pageSize = 10;

    /**
     * 总的查询
     */
    public Integer count = -1;
}
