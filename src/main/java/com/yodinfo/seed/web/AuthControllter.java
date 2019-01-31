package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.LoginInfo;
import com.yodinfo.seed.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api("鉴权认证")
@RequestMapping("/auth/")
public class AuthControllter {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object doSignIn(@ApiParam(value = "验证信息", required = true) @RequestBody @Valid LoginInfo loginInfo) {
        return loginInfo; //TODO
    }

    @ApiOperation(value = "用户注销", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "query")
    })
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object doLogOff(@RequestParam Long id) {
        return null; //TODO
    }
}
