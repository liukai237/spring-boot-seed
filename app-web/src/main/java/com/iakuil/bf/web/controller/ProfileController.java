package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.annotation.Password;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.TokenService;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.shiro.SessionService;
import com.iakuil.bf.web.vo.ProfileEdit;
import com.iakuil.toolkit.BeanUtils;
import com.iakuil.toolkit.PasswordHash;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户设置
 *
 * @author Kai
 */
@Api(value = "ProfileController", tags = {"用户设置"})
@Slf4j
@RestController
@RequestMapping("/api/profile/")
public class ProfileController extends BaseController {

    private final TokenService tokenService;

    private final UserService userService;

    private final SessionService sessionService;

    public ProfileController(TokenService tokenService, UserService userService, SessionService sessionService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @ApiOperation(value = "修改密码", notes = "用户主动修改密码。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pwd", value = "新密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/changePwd")
    public Resp<?> changePwd(@RequestParam String pwd) {
        User user = new User();
        user.setId(getCurrentUserId());
        user.setPasswdHash(PasswordHash.createHash(pwd));
        return ok(userService.modifyWithVersion(user));
    }

    @ApiOperation(value = "修改用户名", notes = "修改用户名。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/changeUsername")
    public Resp<?> changeUsername(@Password @RequestParam String username) {
        //TODO... 用户名校验
        User user = new User();
        user.setId(getCurrentUserId());
        user.setUsername(username);
        return ok(userService.modifyWithVersion(user));
    }

    @ApiOperation(value = "修改手机", notes = "修改用户手机。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tel", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "短信验证码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/changeTel")
    public Resp<?> changeTel(@RequestParam String tel, @RequestParam String smsCode) {
        tokenService.verifySmsCode(tel, smsCode);
        User user = new User();
        user.setTel(tel);
        //TODO 刷新用户认证缓存
        return ok(userService.modifyWithVersion(user));
    }

    @ApiOperation(value = "修改邮箱", notes = "修改用户邮箱。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "电子邮箱", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/changeMail")
    public Resp<?> changeMail(@RequestParam String email) {
        User user = new User();
        user.setId(getCurrentUserId());
        user.setEmail(email);
        //TODO 校验邮件格式，发送确认邮件，刷新权限
        return ok(userService.modifyWithVersion(user));
    }

    @ApiOperation(value = "修改资料", notes = "修改用户非核心资料。")
    @PostMapping(value = "/change")
    public Resp<?> changeOther(@ApiParam(value = "修改个人资料参数", required = true) @Valid @RequestBody ProfileEdit param) {
        User entity = BeanUtils.copy(param, User.class);
        entity.setId(getCurrentUserId());
        return ok(userService.modifyWithVersion(entity));
    }
}
