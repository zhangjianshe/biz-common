package cn.mapway.biz.constant;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * 对ErrorCode提供统一的访问方法,各个子系统和子模块在定义各自的错误编码时可以实现这个接口.
 * 本接口和 <code>ApiResult</code> 进行了集成
 *
 * @author zhangjs2@ziroom.com
 */
public interface IBizCode extends Serializable {

    /**
     * 获取返回码
     *
     * @return data
     */
    Integer getCode();

    /**
     * 获取消息
     *
     * @return data
     */
    String getMessage();

    /**
     * 对消息模板利用参数进行格式化 构造新的对象
     *
     * @param values 模板的参数
     * @return object
     */
    default IBizCode bind(String... values) {
        return new BizCode(getCode(), MessageFormat.format(getMessage(), values));
    }

}
