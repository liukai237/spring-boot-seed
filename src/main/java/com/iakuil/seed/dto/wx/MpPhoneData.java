package com.iakuil.seed.dto.wx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MpPhoneData {
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private Watermark watermark;
}
