package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.UserDetails;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.TokenService;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.web.vo.UserAdd;
import com.iakuil.bf.web.vo.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 注册认证接口
 *
 * @author Kai
 */
@Api(value = "AuthController", tags = {"注册认证"})
@Slf4j
@RestController
@RequestMapping("/api/auth/")
public class AuthController extends BaseController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @ApiOperation(value = "用户登录", notes = "系统用户通过用户名密码登录。")
    @PostMapping(value = "/signIn")
    public Resp<UserDetails> doSignIn(@ApiParam(value = "加密数据") @RequestBody @Valid UserLogin params) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(params.getUsername(), params.getPassword(), params.getRememberMe()));
        return ok((UserDetails) subject.getPrincipal());
    }

    @ApiOperation(value = "用户登出", notes = "用户登出。")
    @PostMapping(value = "/signOut")
    public Resp<?> doSignOut() {
        SecurityUtils.getSubject().logout();
        return ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户通过手机进行注册")
    @PostMapping(value = "/signUp")
    public Resp<?> doSignUp(@ApiParam(value = "注册资料", required = true) @Valid @RequestBody UserAdd param) {
        String tel = param.getTel();
        // 首先校验短信验证码
        tokenService.verifySmsCode(tel, param.getSmsCode());
        User user = new User();
        user.setTel(tel);
        user.setPasswdHash(param.getPassword());
        return ok(userService.register(user));
    }

    @ApiOperation(value = "用户注销", notes = "注销用户")
    @PostMapping(value = "/signOff")
    public Resp<?> doSignOff() {
        return ok(userService.removeById(getCurrentUserId()));
    }
}
