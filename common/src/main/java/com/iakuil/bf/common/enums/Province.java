package com.iakuil.bf.common.enums;

import com.google.common.collect.Maps;
import com.iakuil.bf.common.dict.DictEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

/**
 * 省份枚举
 *
 * @author Kai
 */
@Getter
@AllArgsConstructor
public enum Province implements DictEnum {
    BJ(110000, "北京", "北京市"),
    TJ(120000, "天津", "天津市"),
    HE(130000, "河北", "河北省"),
    SX(140000, "山西", "山西省"),
    NM(150000, "内蒙古", "内蒙古自治区"),
    LN(210000, "辽宁", "辽宁省"),
    JL(220000, "吉林", "吉林省"),
    HL(230000, "黑龙江", "黑龙江省"),
    SH(310000, "上海", "上海市"),
    JS(320000, "江苏", "江苏省"),
    ZJ(330000, "浙江", "浙江省"),
    AH(340000, "安徽", "安徽省"),
    FJ(350000, "福建", "福建省"),
    JX(360000, "江西", "江西省"),
    SD(370000, "山东", "山东省"),
    HA(410000, "河南", "河南省"),
    HB(420000, "湖北", "湖北省"),
    HN(430000, "湖南", "湖南省"),
    GD(440000, "广东", "广东省"),
    GX(450000, "广西", "广西壮族自治区"),
    HI(460000, "海南", "海南省"),
    CQ(500000, "重庆", "重庆市"),
    SC(510000, "四川", "四川省"),
    GZ(520000, "贵州", "贵州省"),
    YN(530000, "云南", "云南省"),
    XZ(540000, "西藏", "西藏自治区"),
    SN(610000, "陕西", "山西省"),
    GS(620000, "甘肃", "甘肃省"),
    QH(630000, "青海", "青海省"),
    NX(640000, "宁夏", "宁夏回族自治区"),
    XJ(650000, "新疆", "新疆维吾尔自治区"),
    TW(710000, "台湾", "台湾省"),
    HK(810000, "香港", "香港特别行政区"),
    MO(820000, "澳门", "澳门特别行政区"),
    QT(999999, "其他", "其他");

    private Integer value;
    private String name;
    private String fullName;

    private static final Map<Integer, Province> LOOKUP = Maps.uniqueIndex(
            Arrays.asList(Province.values()),
            Province::getValue
    );

    public static Province fromCode(Integer code) {
        return LOOKUP.get(code);
    }

    @Override
    public String description() {
        return "省份";
    }
}