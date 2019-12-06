package com.yodinfo.seed.web;

import com.google.common.collect.Maps;
import com.yodinfo.seed.bo.AuthToken;
import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.service.AuthTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api("鉴权认证接口")
@RestController
@RequestMapping("/auth/")
public class AuthController extends BaseController {

    @Resource
    private AuthTokenService authTokenService;

    @ApiOperation(value = "申请token", notes = "申请token，校验优先级为：设备码（比如mac地址）、短信、密码和第三方平台（比如微信扫码登录）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户标识", required = true, dataType = "String", paramType = "query", example = "13838381438"),
            @ApiImplicitParam(name = "passphrase", value = "认证凭证，比如密码、微信jsCode等", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "device", value = "设备类型", defaultValue = "webapp", dataType = "String", paramType = "query", example = "ios", allowableValues = "webapp,wx_webapp,wxmp,ios,android")
    })
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<AuthToken> applyAccessToken(@RequestParam String uid,
                                            @RequestParam(required = false) String passphrase,
                                            @RequestParam(required = false, defaultValue = "webapp") String device) {
        Map<String, String> data = Maps.newHashMap();
        data.put("device", device);
        return ok(authTokenService.generate(uid, data));
    }

    @ApiOperation(value = "刷新token", notes = "通过refresh token刷新access token。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "refreshToken", value = "Refresh Token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<AuthToken> refreshAccessToken(@RequestParam String uid, @RequestParam String refreshToken) {
        return ok(authTokenService.refresh(uid, refreshToken));
    }


    @ApiOperation(value = "清除token", notes = "用户主动注销，并销毁token。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Access Token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/token/clear", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<?> clearAccessToken(@RequestParam String token) {
        authTokenService.clear(token);
        return ok();
    }
}
