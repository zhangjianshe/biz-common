package cn.mapway.biz.api;

import cn.mapway.biz.constant.IBizCode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 接口调用返回列表包装类
 * Link {ApiResult}
 *
 * @param <T> 包装类型
 * @author zhangjianshe
 */

@Data
public class ApiListResult<T> implements Serializable {
    /**
     * 分页的大小
     */
    private Long pageSize;
    /**
     * 查询总条数
     */
    private Long total;
    /**
     * 当前搜索的页
     */
    private Long page;
    private Integer code;
    private String message;
    private List<T> data;

    /**
     * 根据ErrorCode 创建返回结果集
     *
     * @param <T>           返回的数据实体类型
     * @param resultCode    返回码枚举接口
     * @param dataParameter 可以为 null
     * @param parameters    返回消息需要格式化的参数
     * @return data
     */
    public static <T> ApiListResult<T> result(IBizCode resultCode, T dataParameter, String parameters) {
        Integer code = resultCode.getCode();
        String message = resultCode.bind(parameters).getMessage();
        return result(code, message, dataParameter);
    }

    /**
     *
     * @param code code
     * @param message message
     * @param dataParameter data
     * @param <T> data
     * @return data
     */
    public static <T> ApiListResult<T> result(Integer code, String message, T dataParameter) {
        ApiListResult result = new ApiListResult();
        result.setCode(code);
        result.setMessage(message);
        if (dataParameter != null) {
            if (Collection.class.isAssignableFrom(dataParameter.getClass())) {
                Collection collection = (Collection) dataParameter;
                ArrayList list = new ArrayList<>();
                list.addAll(collection);
                result.setData(list);
                result.setTotal(0l + collection.size());
                result.setPageSize(20L);
                result.setPage(1L);
            } else {
                result.setData(new ArrayList());
                result.getData().add(dataParameter);
                result.setPageSize(10L);
                result.setTotal(1L);
                result.setPage(1L);
            }
        } else {
            result.setData(new ArrayList());
            result.setPageSize(20L);
            result.setTotal(0L);
            result.setPage(0L);
        }
        return result;
    }

    /**
     * 创建空的返回结果集，使用者需要填写 code message data 三个字段值
     *
     * @return data
     */
    public static ApiListResult create() {
        return new ApiListResult();
    }

    /**
     * 返回代码Code 为200的代码返回结果
     * @param data data
     * @param <T> return type
     * @return data
     */
    public static <T> ApiListResult success(T data) {
        return result(SystemCodeEnum.SUCCESS, data, null);
    }


    /**
     * 返回代码Code 为500的代码返回结果
     * @param message message
     * @param <T> data type
     * @return data
     */
    public static <T> ApiListResult error(String message) {
        return result(SystemCodeEnum.FAIL, null, message);
    }
}

