package com.iakuil.bf.common;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 字典项
 * <p>用于统一缓存所有的数据字典。</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DictItem extends BaseDomain {
    /**
     * 字典类型编码
     */
    private String type;

    /**
     * 字典类型描述
     */
    private String description;

    /**
     * 字段的value值
     */
    private String value;

    /**
     * 字段的翻译值
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;
}
