package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.service.dto.BasicUserInfoDto;
import com.iakuil.bf.web.dto.UserLoginParam;
import com.iakuil.bf.shiro.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(value = "AuthController", tags = {"用户认证及授权接口"})
@Slf4j
@RestController
@RequestMapping("/api/auth/")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "用户登录", notes = "系统用户通过用户名密码登录。")
    @PostMapping(value = "/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<BasicUserInfoDto> doSignIn(@ApiParam(value = "加密数据") @RequestBody @Valid UserLoginParam params) {
        return ok(authService.signIn(params.getUsername(), params.getPassword(), params.getRememberMe()));
    }

    @ApiOperation(value = "用户注销", notes = "用户注销。")
    @PostMapping(value = "/singOut", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doSingOut() {
        authService.logOut();
        return ok();
    }
}
