package cn.mapway.biz.core;

/**
 * 业务处理约束
 *
 * @author zhangjianshe
 */
public interface IBizExecutor {


    /**
     * 一个业务模块的核心执行逻辑
     *
     * @param bizParam 执行需要的参数
     * @param <R>      返回值类型
     * @param <P>      请求参数
     * @return 业务执行的结果
     */
    <R, P extends BizParam> BizResult<R> execute(P bizParam);
}
