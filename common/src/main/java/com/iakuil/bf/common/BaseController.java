package com.iakuil.bf.common;

import com.iakuil.bf.common.constant.RespCode;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.tool.Strings;
import com.iakuil.toolkit.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 视图层基类
 *
 * <p>所有RestController强制继承。
 *
 * <p>关于数据校验：
 * <p>基础数据校验使用JSR 303注解自动完成，通过{@link com.iakuil.bf.common.annotation.ErrorCode}注解可以返回指定错误码；
 * <p>Controller业务逻辑校验结果应该通过`fail()`方法尽早返回；
 * <p>Service层的校验错误直接抛出{@code BusinessException}即可。
 *
 * @author Kai
 */
public abstract class BaseController {

    public <T> Resp<T> ok() {
        return ok(null);
    }

    public <T> Resp<T> ok(T data) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage(), data);
    }

    public <T> Resp<T> ok(String msg) {
        return new Resp<>(RespCode.SUCCESS.getCode(), RespCode.SUCCESS.getMessage());
    }

    public <T> Resp<T> fail() {
        return new Resp<>(RespCode.FAIL.getCode(), RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public <T> Resp<T> fail(int code) {
        return new Resp<>(code, RespCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public <T> Resp<T> fail(String msg) {
        return new Resp<>(RespCode.FAIL.getCode(), msg);
    }

    public <T> Resp<T> fail(RespCode respCode) {
        return new Resp<>(respCode.getCode(), respCode.getMessage());
    }

    public <T> Resp<T> done(boolean result) {
        return result ? ok() : fail();
    }

    /**
     * 转换为Entity对象，并填充分页排序属性
     */
    protected <R extends Pageable> R toEntity(PageRequest<?> pq, Class<R> clazz) {
        R params = (R) BeanUtils.copy(pq.getFilter(), clazz);
        PageRequest.Paging paging = pq.getPaging();
        PageRequest.Sorting[] sorting = pq.getSorting();
        String orderBy;
        if (sorting != null) {
            // 驼峰转下划线，逗号分隔
            orderBy = Arrays.stream(sorting)
                    .filter(item -> StringUtils.isNoneBlank(item.getField()))
                    .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                    .collect(Collectors.joining(Strings.COMMA));
            params.setOrderBy(orderBy);
        }
        if (paging != null) {
            Integer pageSize = ObjectUtils.defaultIfNull(paging.getPageSize(), SysConstant.DEFAULT_PAGE_SIZE);
            params.setPageSize(pageSize > SysConstant.MAX_PAGE_SIZE ? SysConstant.MAX_PAGE_SIZE : pageSize);
            params.setPageNum(ObjectUtils.defaultIfNull(paging.getPageNum(), 1));
        }
        return params;
    }
}