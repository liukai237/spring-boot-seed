package com.iakuil.seed.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iakuil.seed.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 简单封装的RestTemplate
 **/
@Slf4j
@Component
public class WxRestTemplate {

    private final RestTemplate restTemplate;

    public WxRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8), new ByteArrayHttpMessageConverter()).build();
    }

    public String get(String url, Object... params) {
        return restTemplate.getForObject(url, String.class, params);
    }

    public <T> T get(Class<T> clazz, String url, Object... params) {
        return transfer(get(url, params), clazz);
    }

    public String post(String url, String json, Object... params) {
        return restTemplate.postForObject(url, json, String.class, params);
    }

    public <T> T post(Class<T> clazz, String url, String json, Object... params) {
        return transfer(post(url, json, params), clazz);
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


    public String upload(String apiUrl, File file, Object... params) {
        return upload(apiUrl, new FileSystemResource(file), params);
    }

    /**
     * 上传文件，默认分片
     **/
    private String upload(String apiUrl, AbstractResource resource, Object... params) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("media", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> parts = new HttpEntity<>(body, headers);
        return new RestTemplate().postForObject(apiUrl, parts, String.class, params);
    }

    /**
     * 下载文件并重命名
     **/
    public String download(String url, String localFilename) {
        ClientHttpRequestInterceptor interceptor = new DownloadInterceptor();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

        String tempFilePath = "";
        if (responseEntity.getBody() != null) {
            File tmpFile;
            try {
                tmpFile = FileUtils.getFile("/tmp", localFilename);
                FileUtils.touch(tmpFile);
                FileUtils.writeByteArrayToFile(tmpFile, responseEntity.getBody());
                tempFilePath = tmpFile.getAbsolutePath();
            } catch (IOException e) {
                log.error("Occurring an exception during file downloading!");
                throw new BusinessException("Occurring an exception during file downloading!", e);
            }
        }
        return tempFilePath;
    }

    /**
     * 下载文件(POST方式)
     **/
    public byte[] downloadWithPost(String url, String json, Object... params) {
        restTemplate.setInterceptors(Collections.singletonList(new DownloadInterceptor()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class, params);

        byte[] body;
        if (response.getStatusCode() == HttpStatus.OK) {
            body = response.getBody();
        } else {
            log.error("Occurring an exception during file downloading!");
            throw new ResourceAccessException(StringUtils.newStringUtf8(response.getBody()));
        }

        return body;
    }

    public static class DownloadInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            requestWrapper.getHeaders().setAccept(Collections.singletonList(MediaType.ALL));
            return execution.execute(requestWrapper, body);
        }
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