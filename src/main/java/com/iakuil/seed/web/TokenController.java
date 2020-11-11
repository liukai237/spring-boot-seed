package com.iakuil.seed.web;

import com.iakuil.seed.common.BaseController;
import com.iakuil.seed.common.Resp;
import com.iakuil.seed.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Api(value = "Token生成与校验", tags = {"Token生成与校验"})
@Slf4j
@RestController
@RequestMapping("/api/token/")
public class TokenController extends BaseController {

    @Autowired
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @ApiOperation(value = "获取Token", notes = "获取一次性Token（调用校验方法后无论是否匹配均删除）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sec", value = "缓存时间（分钟）", dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> getSimpleToken(@RequestParam(required = false) Long sec) {
        return ok(sec == null ? tokenService.getSimpleToken() : tokenService.getSimpleToken(sec, TimeUnit.MINUTES));
    }

    @ApiOperation(value = "校验Token", notes = "校验Token，调试用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "需要验证的Token", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> checkSimple(@RequestParam String token) {
        tokenService.checkSimple(token);
        return ok();
    }

    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码")
    @GetMapping(value = "/captcha")
    public Resp<?> renderCaptcha() {
        tokenService.renderCaptcha();
        return ok();
    }

    @ApiOperation(value = "校验图形验证码", notes = "校验图形验证码，调试用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "需要验证的Code", required = true, dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> checkCaptcha(@RequestParam String code) {
        tokenService.validCaptcha(code);
        return ok();
    }

    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码（需要先获取短信验证码）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "需要验证的手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "captcha", value = "图形验证码", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping(value = "/smsCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> getSmsCode(String tel, String captcha) {
        tokenService.validCaptcha(captcha);
        tokenService.sendSmsCode(tel);
        return ok();
    }

    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码，调试用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "需要验证的手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "需要验证的短信验证码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/smsCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> checkCaptcha(@RequestParam String tel, @RequestParam String code) {
        tokenService.verifySmsCode(tel, code);
        return ok();
    }
}
