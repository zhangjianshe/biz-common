package cn.mapway.biz.core;


import cn.mapway.biz.api.ApiListResult;
import cn.mapway.biz.api.ApiResult;
import cn.mapway.biz.api.SystemCodeEnum;
import cn.mapway.biz.constant.BizCode;
import cn.mapway.biz.constant.IBizCode;
import cn.mapway.biz.exception.BizException;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 业务流返回对象的基类
 */
@Data
public class BizResult<T> implements Serializable {
    private Integer code;
    private String message;
    private List<IBizCode> errorList;
    private BizFlowOperator flowOperator;

    private T data;
    private long total;
    private long current;
    private long pageSize;

    /**
     * 构造一个默认成功的操作结果,缺省继续执行下一个节点
     */
    public BizResult() {
        this(SystemCodeEnum.SUCCESS);
    }

    /**
     * @param bizCode  bizCode
     * @param messages messages
     */
    public BizResult(IBizCode bizCode, String... messages) {
        flowOperator = BizFlowOperator.BIZ_CONTINUE;
        setErrorList(null);
        result(bizCode, messages);
        total = 0L;
        current = 1;
        pageSize = 10;
    }

    /**
     * @param code    code
     * @param message message
     */
    public BizResult(Integer code, String message) {
        flowOperator = BizFlowOperator.BIZ_CONTINUE;
        setErrorList(null);
        this.code = code;
        this.message = message;
        total = 0L;
        current = 1;
        pageSize = 10;
    }

    /**
     * 构造结果
     *
     * @param bizCode  bizCode
     * @param messages messages
     * @return data
     */
    public static BizResult error(IBizCode bizCode, String... messages) {
        return new BizResult(bizCode, messages);
    }

    /**
     * 直接输入code和消息
     *
     * @param code    coder
     * @param message message
     * @return data
     */
    @Deprecated
    public static BizResult error(Integer code, String message) {
        return new BizResult(code, message);
    }

    /**
     * 构造结果
     *
     * @param data object
     * @return data
     */
    public static BizResult success(Object data) {
        BizResult result = new BizResult(SystemCodeEnum.SUCCESS);
        result.setData(data);
        return result;
    }


    /**
     * 构造一个空的数据集
     *
     * @return data
     */
    public static BizResult emptyList() {
        BizResult result = new BizResult(SystemCodeEnum.SUCCESS);
        result.setListInfo(0l, 1l, 100l);
        result.setData(new ArrayList<>());
        return result;
    }

    /**
     * @param bizCode bizcode
     * @param data    data
     * @return data
     */
    public static BizResult create(IBizCode bizCode, Object data) {
        BizResult result = new BizResult(bizCode);
        result.setData(data);
        return result;
    }


    /**
     * @return ApiResult
     */
    public ApiResult toApiResult() {
        return ApiResult.result(code, message, data);
    }


    /**
     * @return ApiListResult
     */
    public ApiListResult toApiListResult() {
        ApiListResult result = ApiListResult.result(code, message, data);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setPage(current);
        return result;
    }


    /**
     * @return T d
     */
    public T getData() {
        return data;
    }


    /**
     * @param data data
     */
    public void setData(T data) {
        this.data = data;
    }


    /**
     * @param data data
     * @return data
     */
    public BizResult<T> withData(T data) {
        success();
        this.data = data;
        return this;
    }

    /**
     * 设置错误代码
     *
     * @param bizError bizError
     * @param messages messages
     */
    public void result(IBizCode bizError, String... messages) {
        this.code = bizError.getCode();
        this.message = bizError.bind(messages).getMessage();
    }


    /**
     * @return IBizCode
     */
    public IBizCode getBizCode() {
        return new BizCode(this.getCode(), this.getMessage());
    }


    /**
     * @return BizResult
     */
    public BizResult success() {
        result(SystemCodeEnum.SUCCESS);
        return this;
    }

    /**
     * 设置一个失败的错误代码
     *
     * @param bizError bizError
     * @return data
     */
    public BizResult fail(IBizCode bizError) {
        result(bizError);
        return this;
    }


    /**
     * @return Boolean
     */
    public Boolean isSuccess() {
        return Objects.equals(code, SystemCodeEnum.SUCCESS.getCode());
    }


    /**
     * @return Boolean
     */
    public Boolean isFailed() {
        return !isSuccess();
    }


    /**
     * @return Boolean
     */
    public Boolean needBreak() {
        if (isSuccess()) {
            return false;
        }
        return Objects.equals(flowOperator, BizFlowOperator.BIZ_BREAK);
    }


    /**
     * @return Boolean
     */
    public Boolean needRollback() {
        if (isSuccess()) {
            return false;
        }
        return Objects.equals(flowOperator, BizFlowOperator.BIZ_ROLLBACK);
    }


    /**
     * @return error list
     */
    public List<IBizCode> getErrorList() {
        return errorList;
    }


    /**
     * @param errorList error list
     */
    public void setErrorList(List<IBizCode> errorList) {
        if (null == errorList) {
            this.errorList = new ArrayList<>();
        }
    }


    /**
     * @param bizError bizErrror
     */
    public void addError(IBizCode bizError) {
        this.errorList.add(bizError);
    }


    /**
     * @return long
     */
    public long getPageSize() {
        return pageSize;
    }


    /**
     * @param pageSize pageSiz
     */
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }


    /**
     * @return long
     */
    public long getCurrent() {
        return current;
    }

    /**
     * @param current current
     */
    public void setCurrent(long current) {
        this.current = current;
    }

    /**
     * 设置返回值的列表信息
     *
     * @param total    total
     * @param current  current
     * @param pageSize pageSize
     */
    public void setListInfo(Long total, Long current, Long pageSize) {
        this.current = current == null ? 1 : current;
        this.total = total == null ? 0 : total;
        this.pageSize = pageSize == null ? 10 : pageSize;
    }

    /**
     * 将结果通过异常的形式抛出
     */
    public void throwException() {
        throw BizException.get(getCode(), getMessage());
    }
}
