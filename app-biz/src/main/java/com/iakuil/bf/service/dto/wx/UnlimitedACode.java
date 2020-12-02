package com.iakuil.bf.service.dto.wx;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UnlimitedACode {
    private String scene;
    private String page;
    private Integer width;
    @JsonProperty("auto_color")
    private Boolean autoColor;
    @JsonProperty("line_color")
    private Map<String, Object> lineColor;
    @JsonProperty("is_hyaline")
    private Boolean isHyaline;
}