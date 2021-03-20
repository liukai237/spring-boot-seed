package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.TokenService;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.shiro.SessionService;
import com.iakuil.bf.web.vo.PwdEdit;
import com.iakuil.bf.web.vo.SmsLogin;
import com.iakuil.bf.web.vo.UserLogin;
import com.iakuil.toolkit.PasswordHash;
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
 * 认证授权接口
 *
 * @author Kai
 */
@Api(value = "AuthController", tags = {"认证授权"})
@Slf4j
@RestController
@RequestMapping("/api/auth/")
public class AuthController extends BaseController {

    private final UserService userService;
    private final TokenService tokenService;
    private final SessionService sessionService;

    public AuthController(UserService userService, TokenService tokenService, SessionService sessionService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.sessionService = sessionService;
    }

    @ApiOperation(value = "密码登录", notes = "系统用户通过用户名/手机号码/邮箱+密码登录。")
    @PostMapping(value = "/signIn")
    public Resp<UserDetails> doSignIn(@ApiParam(value = "加密数据") @RequestBody @Valid UserLogin params) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(params.getUsername(), params.getPassword(), params.getRememberMe()));
        UserDetails details = (UserDetails) subject.getPrincipal();
        sessionService.kickOutFor(details.getId());
        return ok(details);
    }

    @ApiOperation(value = "短信登录", notes = "系统用户通过手机验证码登录。")
    @PostMapping(value = "/signInBySms")
    public Resp<UserDetails> signInBySms(@ApiParam(value = "加密数据") @RequestBody @Valid SmsLogin params) {
        //TODO SmsRealm、发送短信验证码
        return ok();
    }

    @ApiOperation(value = "忘记密码", notes = "用户忘记密码后通过短信验证码重置密码。")
    @PostMapping(value = "/resetPwd")
    public Resp<UserDetails> resetPwdBySms(@ApiParam(value = "加密数据") @RequestBody @Valid PwdEdit params) {
        String tel = params.getTel();
        tokenService.verifySmsCode(tel, params.getSmsCode());
        User user = new User();
        user.setPasswdHash(PasswordHash.createHash(params.getPassword()));
        return ok();
    }

    @ApiOperation(value = "用户登出", notes = "用户登出。")
    @PostMapping(value = "/signOut")
    public Resp<?> doSignOut() {
        SecurityUtils.getSubject().logout();
        return ok();
    }
}
