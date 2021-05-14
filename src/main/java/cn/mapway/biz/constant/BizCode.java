package cn.mapway.biz.constant;
import java.text.MessageFormat;

/**
 * 业务代码的缺省实现
 *
 * @author zhangjianshe
 */
public class BizCode implements IBizCode {
    String message;
    Integer code;

    /**
     *
     * @param code code
     * @param message message
     */
    public BizCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * get code
     * @return code
     */
    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     * ge tmessage
     * @return data
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param values 模板的参数
     * @return object
     */
    @Override
    public IBizCode bind(String... values) {
        return new BizCode(getCode(), MessageFormat.format(getMessage(), values));
    }
}
