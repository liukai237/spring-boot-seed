package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.TokenService;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.web.vo.UserAdd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户注册
 *
 * @author Kai
 */
@Api(value = "RegisterController", tags = {"用户注册"})
@Slf4j
@RestController
@RequestMapping("/api/reg/")
public class RegisterController extends BaseController {

    private final TokenService tokenService;

    private final UserService userService;

    public RegisterController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @ApiOperation(value = "账号注册", notes = "通过手机注册账号。")
    @PostMapping(value = "/signUp")
    public Resp<?> doSignUp(@ApiParam(value = "注册资料", required = true) @Valid @RequestBody UserAdd param) {
        String tel = param.getTel();
        // 首先校验短信验证码
        tokenService.verifySmsCode(tel, param.getSmsCode());
        User user = new User();
        user.setTel(tel);
        user.setPasswdHash(param.getPassword());
        return ok(userService.add(user));
    }
}
