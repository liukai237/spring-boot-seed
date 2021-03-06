package com.iakuil.bf.common.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.constant.SysConstant;
import com.iakuil.bf.common.db.Condition;
import com.iakuil.bf.common.tool.Strings;
import com.iakuil.toolkit.BeanMapUtils;
import com.iakuil.toolkit.BeanUtils;
import com.iakuil.toolkit.MapBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分页排序查询请求体
 *
 * <p>通过{@link #as}方法转换为实体类对象；
 * <p>通过{@link #wrap}方法转换为Condition查询对象；
 * <p>两个方法都可以自动填充分页排序字段。
 *
 * @param <T> 查询参数对象类型
 * @author Kai
 */
@ApiModel(value = "PageRequest", description = "统一封装的分页排序请求体")
public class PageRequest<T> implements Serializable {

    @ApiModelProperty(name = "filtering", value = "过滤参数。")
    private T filter;

    @ApiModelProperty(value = "paging", notes = "分页参数。")
    private Paging paging;

    @ApiModelProperty(value = "sorting", notes = "排序参数。")
    private Sorting[] sorting;

    @ApiModelProperty(hidden = true)
    private Map<String, Object> other = new HashMap<>();

    @JsonAnySetter
    public void setOther(String key, Object value) {
        this.other.put(key, value);
    }

    /**
     * 转换为Query对象，自动填充分页排序参数
     *
     * <p>如果是单表通用查询，入参一般是实体类；
     * <p>如果是复杂查询，Query对象应该继承{@code Pageable}，并且放在Service层dto目录下。
     * <br/>
     * <p>e.g.
     * <pre>{@code
     *  public Resp<PageData<UserDto>> doQueryWithPage(@RequestBody PageRequest<UserQuery> req) {
     *    return ok(userService.page(req.as(User.class), UserConverter.INSTANCE::toDto));
     *  }
     * }</pre>
     */
    public <R extends Pageable> R as(Class<R> clazz) {
        R condition = BeanUtils.copy(this.getFilter(), clazz);

        PageRequest.Paging paging = this.getPaging();
        if (paging == null) {
            // workaround，有些前端分页组件分页参数放在第一层
            condition.setPageNum((int) this.other.get(SysConstant.DEFAULT_PAGE_NUM_FIELD));
            condition.setPageSize((int) this.other.get(SysConstant.DEFAULT_PAGE_SIZE_FIELD));
        } else {
            condition.setPageNum(paging.getPageNum());
            condition.setPageSize(paging.getPageSize());
        }

        PageRequest.Sorting[] sorting = this.getSorting();
        if (sorting == null) {
            condition.setOrderBy(Strings.parseOrderBy((String) this.other.get(SysConstant.DEFAULT_SORT_PARAM)));
        } else {
            condition.setOrderBy(handleOrderBy(sorting));
        }

        return condition;
    }

    /**
     * 包装为Condition对象，自动填充分页排序参数
     *
     * <p>如果没有范围查询之类的特殊需求，优先使用使用{@link PageRequest#as(Class)}方法。
     * <p>e.g.
     * <pre>{@code
     *  public Resp<PageData<UserDto>> doQueryWithPage(@RequestBody PageRequest<UserQuery> req) {
     *    Condition condition = request.wrap(User.class);
     *    Condition.Criteria criteria = condition.createCriteria();
     *    criteria.andGreaterThan("id", 0);
     *    condition.and(criteria);
     *    return ok(userService.page(condition, UserConverter.INSTANCE::toDto));
     *  }
     * }</pre>
     */
    public <R extends Pageable> Condition wrap(Class<R> clazz) {
        Condition condition = new Condition(clazz, false, false);
        Condition.Criteria criteria = condition.createCriteria();
        Map<String, Object> params = MapBuilder.init(BeanMapUtils.beanToMap(this.getFilter(), true)).build();
        if (params.isEmpty()) {
            criteria.andCondition("1 = 1");
        } else {
            criteria.andAllEqualTo(params);
        }

        Paging paging = this.getPaging();
        if (paging != null) {
            condition.setPageNum(paging.getPageNum());
            condition.setPageSize(paging.getPageSize());
        }

        PageRequest.Sorting[] sorting = this.getSorting();
        if (sorting != null) {
            condition.setOrderByClause(handleOrderBy(sorting));
        }

        return condition;
    }

    private String handleOrderBy(Sorting[] sorting) {
        // 驼峰转下划线，逗号分隔
        return Arrays.stream(sorting)
                .filter(item -> StringUtils.isNoneBlank(item.getField()))
                .map(item -> Strings.toUnderlineCase(item.getField()) + Strings.SPACE + item.getOrder().toString())
                .collect(Collectors.joining(Strings.COMMA));
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Sorting[] getSorting() {
        return sorting;
    }

    public void setSorting(Sorting[] sorting) {
        this.sorting = sorting;
    }

    /**
     * 分页参数
     * <p>合法pageSize范围：10到500
     */
    @Getter
    @Setter
    @ApiModel(value = "Paging", description = "分页参数")
    public static class Paging {
        @ApiModelProperty(name = SysConstant.DEFAULT_PAGE_NUM_FIELD, value = "分页页码", example = "1")
        @JsonProperty(SysConstant.DEFAULT_PAGE_NUM_FIELD)
        private Integer pageNum;
        @ApiModelProperty(name = SysConstant.DEFAULT_PAGE_SIZE_FIELD, value = "分页尺寸", example = "10")
        @JsonProperty(SysConstant.DEFAULT_PAGE_SIZE_FIELD)
        private Integer pageSize;

        public Integer getPageSize() {
            return pageSize == null ? SysConstant.DEFAULT_PAGE_SIZE
                    : (pageSize > SysConstant.MAX_PAGE_SIZE ? SysConstant.MAX_PAGE_SIZE : pageSize);
        }

        public Integer getPageNum() {
            return pageNum == null ? SysConstant.DEFAULT_PAGE_NUM
                    : (pageNum > SysConstant.MAX_PAGE_NUM ? SysConstant.MAX_PAGE_NUM : pageNum);
        }
    }

    /**
     * 排序参数
     */
    @Getter
    @Setter
    @ApiModel(value = "Sorting", description = "排序参数")
    public static class Sorting {
        @ApiModelProperty(name = "field", value = "排序字段", example = "createTime")
        private String field;
        @ApiModelProperty(name = "order", value = "排序方向", allowableValues = "asc,desc", example = "desc")
        private Direction order;
    }

    @Getter
    public enum Direction {
        ASC, DESC
    }
}