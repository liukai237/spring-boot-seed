package com.iakuil.bf.common;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 字典项
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
     * 字段的value值
     */
    private String value;
    /**
     * 字段的翻译值
     */
    private String name;
}
