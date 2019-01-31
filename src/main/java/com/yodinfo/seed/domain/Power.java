package com.yodinfo.seed.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Power {
    private Long powerId;
    private String powerName;
    //private Long systemId;
    //private Long pid;
    private Date createTime;
}