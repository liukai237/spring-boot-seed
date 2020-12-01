package com.iakuil.service.constant;

public class WxConstant {

    // 基础服务
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";
    public static final String SESSION_KEY_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code";

    // XX码
    public static final String WXACODE_URL = "https://api.weixin.qq.com/wxa/getwxacode?access_token={ACCESS_TOKEN}";
    public static final String WXAQRCODE_URL = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token={ACCESS_TOKEN}";
    public static final String WXAQRCODE_EUNLIMIT_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token={ACCESS_TOKEN}";

    // 订阅消息
    public final static String SEND_SUBSCRIBE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token={ACCESS_TOKEN}";
}