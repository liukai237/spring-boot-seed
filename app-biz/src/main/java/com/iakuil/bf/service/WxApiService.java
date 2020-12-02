package com.iakuil.bf.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.bf.common.exception.BusinessException;
import com.iakuil.bf.common.tool.JsonUtils;
import com.iakuil.bf.service.constant.WxConstant;
import com.iakuil.bf.service.converter.MpUserInfoConverter;
import com.iakuil.bf.service.dto.wx.*;
import com.iakuil.bf.service.util.CryptoDecrypt;
import com.iakuil.bf.service.util.JsonPathUtils;
import com.iakuil.bf.service.util.WxRestTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;

/**
 * 微信接口适配服务
 */
@Slf4j
@Service
public class WxApiService {

    private final WxTokenMgrService wxTokenMgrService;
    private final MpUserInfoService mpUserInfoService;

    @Resource
    private WxRestTemplate restClient;

    @Autowired
    public WxApiService(WxTokenMgrService wxTokenMgrService, MpUserInfoService mpUserInfoService) {
        this.wxTokenMgrService = wxTokenMgrService;
        this.mpUserInfoService = mpUserInfoService;
    }

    /**
     * 解密微信手机数据
     */
    public MpPhoneData decryptPhone(String sessionKey, String data, String iv) {
        return decryptData(sessionKey, data, iv, MpPhoneData.class);
    }

    /**
     * 解密微信用户数据
     * 并持久化到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public MpUserInfoDTO decryptUserInfo(String sessionKey, String data, String iv) {
        MpUserInfoDTO info = decryptData(sessionKey, data, iv, MpUserInfoDTO.class);

        // 持久化用户解密数据
        mpUserInfoService.addOrModifyUserInfo(MpUserInfoConverter.INSTANCE.toEntity(info));
        return info;
    }

    /**
     * 创建小程序码
     */
    public byte[] createACode(ACode aCode) {
        return restClient.downloadWithPost(WxConstant.WXACODE_URL, JsonUtils.bean2Json(aCode), wxTokenMgrService.getToken().getAccessToken());
    }

    /**
     * 创建小程序码(Unlimited)
     */
    public byte[] createUnlimitedACode(UnlimitedACode aCode) {
        return restClient.downloadWithPost(WxConstant.WXAQRCODE_EUNLIMIT_URL, JsonUtils.bean2Json(aCode), wxTokenMgrService.getToken().getAccessToken());
    }

    /**
     * 创建小程序二维码
     */
    public byte[] createAQRCode(ACode code) {
        return restClient.downloadWithPost(WxConstant.WXAQRCODE_URL, JsonUtils.bean2Json(code), wxTokenMgrService.getToken().getAccessToken());
    }

    /**
     * 发送订阅消息
     */
    public R sendSubscribeMsg(SubscribeMsg subscribeMsg, Integer index) {
        return post(R.class, WxConstant.SEND_SUBSCRIBE_MESSAGE_URL, JsonUtils.bean2Json(subscribeMsg), wxTokenMgrService.getToken().getAccessToken());
    }

    private String get(String url, Object... params) {
        String resp = restClient.get(url, params);
        check(resp); // 之所以不直接调用restTemplate已经封装好的get方法是因为此处还要做校验工作
        return resp;
    }

    private <T> T get(Class<T> clazz, String url, Object... params) {
        return transfer(get(url, params), clazz);
    }

    private String post(String url, String json, Object... params) {
        String resp = restClient.post(url, json, params);
        check(resp);
        return resp;
    }

    private <T> T post(Class<T> clazz, String url, String json, Object... params) {
        return transfer(post(url, json, params), clazz);
    }

    private String upload(File file, String url, Object... params) {
        String resp = restClient.upload(url, file, params);
        check(resp);
        return resp;
    }

    private <T> T upload(Class<T> clazz, File file, String url, Object... params) {
        return transfer(upload(file, url, params), clazz);
    }

    private void check(String json) {
        log.info("Response from wechat API: {}", json);
        Integer errorCode = JsonPathUtils.readInt(json, "$.errcode");
        if (json.contains("errcode") && 0 != errorCode) {
            log.error("[Occurring an exception during wechat api invocation!]\n[{}]", json);
            throw new BusinessException("[Occurring an exception during wechat api invocation!]\n" + json);
        }
    }

    private <T> T transfer(String json, Class<T> clazz) {
        if (json.contains("errcode") && json.contains("errmsg") && json.contains("data")) { // 正常的三个字段结构JSON
            R r = JsonUtils.json2bean(json, R.class);
            Object data = r.getData();
            return data == null ? null : JsonUtils.transfer(data, clazz);
        } else {
            return JsonUtils.json2bean(json, clazz);
        }
    }

    private <T> T decryptData(String key, String data, String iv, Class<T> clazz) {
        return JsonUtils.json2bean(CryptoDecrypt.decrypt(key, data, iv), clazz);
    }

    /**
     * 微信返回结果
     * 正常三个字段结构
     */
    @Getter
    @Setter
    private static class R {
        @JsonProperty("errcode")
        private int errCode;
        @JsonProperty("errmsg")
        private String errMsg;
        private Object data;
    }
}