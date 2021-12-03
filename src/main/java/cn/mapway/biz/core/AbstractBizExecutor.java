package cn.mapway.biz.core;

import cn.mapway.biz.exception.BizException;
import org.nutz.lang.Strings;

/**
 * 业务流处理器
 * 负责具体的执行逻辑额,
 * 每一个业务逻辑继承此类，实现onExecute方法
 * 子类继承时需要提供2个模板参数类型
 * <p>
 * 多个Executor之间 如果又相互关联关系，比如传递参数之类的，可以通过 BizContext 参数
 * 设个参数针对每个线程提供不同的实例。
 *
 * @param <R> 返回值的数据类型,业务处理逻辑中会包装这个数据类型
 * @param <P> 请求的参数类型
 */
public abstract class AbstractBizExecutor<R, P> {

    /**
     * 线程相关的变量，用于Executor之间传递参数
     */
    private static final ThreadLocal<BizContext> threadLocalBizContext = ThreadLocal.withInitial(
            () -> {
                return new BizContext();
            }
    );

    /**
     * 这个方法是一个业务节点对外的服务约束
     * 未来会处理这个逻辑
     * 不建议使用,在业务代码中需要手动释放context,不释放容易造成内存泄漏
     *
     * @param request 执行需要的参数
     * @return result
     */
    @Deprecated
    public BizResult<R> execute(BizRequest<P> request) {
        return process(threadLocalBizContext.get(), request);
    }

    /**
     * 扩展方法, 推荐使用该方法，由业务方定义传入
     *
     * @param context context
     * @param request res=quest
     * @return data
     */
    public BizResult<R> execute(BizContext context, BizRequest<P> request) {
        try {
            validateParameter(request.getData());
            return process(context, request);
        } catch (BizException exception) {
            return BizResult.error(exception.getResponse());
        } catch (Exception e) {
            return BizResult.error(500, e.getMessage());
        }
    }

    /**
     * 验证参数 根据 validate
     */
    protected void validateParameter(P parameter) {
        if (parameter == null) {
            throw BizException.get(500, "需要传入参数");
        }
    }

    /**
     * 检查参数是否为空
     *
     * @param data
     * @param message
     */
    public void assertNotNull(Object data, String message) {
        if (data == null) {
            throw BizException.get(500, message);
        }
    }

    /**
     * 检查字符串是否为空
     *
     * @param data
     * @param message
     */
    public void assertNotEmpty(String data, String message) {
        if (Strings.isBlank(data)) {
            throw BizException.get(500, message);
        }
    }

    /**
     * 清除线程数据
     */
    public void removeContext() {
        threadLocalBizContext.remove();
    }

    /**
     * 每一个单独的业务处理单元，都需要实现这个方法,该方法为保护方法，不应该被第三方调用
     * context 可以存储临时变量
     * ？什么时候销毁里面的数据：业务根据自己的需要，可以清空里面的数据
     *
     * @param context  这个参数是执行这次调用的一个容器环境,业务处理单元可以将临时的变量数据存放在这个上下文中
     * @param bizParam 请求的参数 参与业务调用的参数 必须继承自 BizParam
     * @return result
     */
    protected abstract BizResult<R> process(BizContext context, BizRequest<P> bizParam);


}
