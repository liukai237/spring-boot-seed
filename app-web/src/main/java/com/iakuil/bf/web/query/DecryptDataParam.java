package com.iakuil.bf.web.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@ApiModel(value = "DecryptDataParam", description = "解密接口参数")
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecryptDataParam {

    @Length(max = 28, min = 28, message = "openId必须是28个字符")
    @ApiModelProperty(name = "openId", value = "openid，28个字符，小程序范围内唯一。")
    private String openId;

    @ApiModelProperty(name = "data", value = "被加密的数据。")
    @NotNull
    private String data;

    @ApiModelProperty(name = "iv", value = "Initial Vector，24个字符。")
    @Length(max = 24, min = 24, message = "iv必须是24个字符！")
    private String iv;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}