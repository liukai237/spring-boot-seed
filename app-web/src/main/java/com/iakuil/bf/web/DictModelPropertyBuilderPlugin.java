package com.iakuil.bf.web;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import com.iakuil.bf.common.DictItem;
import com.iakuil.bf.common.DictPool;
import com.iakuil.bf.common.annotation.DictType;
import com.iakuil.bf.web.job.DictRefreshingJob;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger文档数据字典插件
 */
@Slf4j
@Component
public class DictModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {
    @Autowired
    private DictRefreshingJob dictRefreshingJob;

    @PostConstruct
    public void init() {
        dictRefreshingJob.refreshDictItems();
    }

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<BeanPropertyDefinition> optional = context.getBeanPropertyDefinition();
        if (!optional.isPresent()) {
            return;
        }

        AnnotatedField field = optional.get().getField();
        addDescForDict(context, field);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void addDescForDict(ModelPropertyContext context, AnnotatedField field) {
        List<DictItem> dictItems = new ArrayList<>();
        Class<?> fieldType = field.getRawType();
        DictType dictType = field.getAnnotation(DictType.class);
        if (dictType != null) {
            dictItems = DictPool.cache.get(dictType.value());
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            Optional<ApiModelProperty> annotation = Optional.absent();
            if (context.getAnnotatedElement().isPresent()) {
                annotation = annotation.or(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
            }
            if (context.getBeanPropertyDefinition().isPresent()) {
                annotation = annotation.or(Annotations.findPropertyAnnotation(
                        context.getBeanPropertyDefinition().get(),
                        ApiModelProperty.class));
                dictItems = DictPool.cache.get(fieldType.getSimpleName());
                String typeDesc = (annotation.get()).value();
                dictItems.forEach(item -> item.setDescription(typeDesc));
            }

            if (annotation.isPresent()) {

            }
        } else {
            // 非数据字典直接忽略
            return;
        }

        if (CollectionUtils.isNotEmpty(dictItems)) {
            ModelPropertyBuilder builder = context.getBuilder();
            String joinText = dictItems.get(0).getDescription() + "（" + (dictItems.stream()
                    .map(item -> item.getValue() + "：" + item.getName()))
                    .collect(Collectors.joining("；"))
                    + "）";
            builder.description(joinText).type(context.getResolver().resolve(Integer.class));
        }
    }
}