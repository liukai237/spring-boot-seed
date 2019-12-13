package com.yodinfo.seed.web;

import com.google.common.collect.Maps;
import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.bo.TokenInfo;
import com.yodinfo.seed.constant.AuthSchema;
import com.yodinfo.seed.constant.DeviceType;
import com.yodinfo.seed.service.AuthTokenService;
import com.yodinfo.seed.service.ClientAuthService;
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

    @Resource
    private ClientAuthService clientAuthService;

    @ApiOperation(value = "申请token", notes = "申请token，校验优先级为：密码、短信和第三方平台（比如微信扫码登录）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户标识", required = true, dataType = "String", paramType = "query", example = "13838381438"),
            @ApiImplicitParam(name = "ticket", value = "认证凭证，比如密码、微信jsCode、短信验证码等", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "辅助验证码，比如图形验证码等", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "device", value = "设备类型", defaultValue = "webapp", dataType = "String", paramType = "query", example = "ios", allowableValues = "webapp,wxoa,wxmp,ios,android"),
            @ApiImplicitParam(name = "schema", value = "认证授权方案", defaultValue = "passwd", dataType = "String", paramType = "query", example = "passwd", allowableValues = "passwd,sms,oauth2,session_key")
    })
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<TokenInfo> applyAccessToken(@RequestParam String uid,
                                            @RequestParam(required = false) String ticket,
                                            @RequestParam(required = false) String code,
                                            @RequestParam(required = false, defaultValue = "webapp") DeviceType device,
                                            @RequestParam(required = false, defaultValue = "passwd") AuthSchema schema) {
        Map<String, String> data = Maps.newHashMap();
        data.put("device", device.getValue()); // one device one token
        clientAuthService.signIn(uid, ticket, code, device, schema);
        return ok(authTokenService.generate(uid, data));
    }

    @ApiOperation(value = "刷新token", notes = "通过refresh token刷新access token。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "refreshToken", value = "Refresh Token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<TokenInfo> refreshAccessToken(@RequestParam String uid, @RequestParam String refreshToken) {
        return ok(authTokenService.refresh(uid, refreshToken));
    }


    @ApiOperation(value = "清除token", notes = "用户主动注销，并销毁token。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Access Token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/token/clear", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<?> clearAccessToken(@RequestParam String token) {
        authTokenService.clear(token);
        clientAuthService.signOut();
        return ok();
    }
}