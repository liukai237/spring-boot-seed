package com.yodinfo.seed.web;

import com.yodinfo.seed.bo.PageData;
import com.yodinfo.seed.bo.Resp;
import com.yodinfo.seed.converter.UserConverter;
import com.yodinfo.seed.domain.User;
import com.yodinfo.seed.dto.BasicUserInfo;
import com.yodinfo.seed.dto.UserRegInfo;
import com.yodinfo.seed.service.UserService;
import com.yodinfo.seed.util.StrUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

@Api(value = "用户管理")
@RestController
public class UserController extends BaseController {

    private final UserConverter userConverter;

    @Resource
    private UserService userService;

    public UserController(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @ApiOperation(value = "用户列表", notes = "根据用户ID来获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", defaultValue = "0", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "排序规则", defaultValue = "createTime-", dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<PageData<BasicUserInfo>> queryUsers(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                    @RequestParam(required = false, defaultValue = "0") Integer pageSize,
                                                    @RequestParam(required = false, defaultValue = "createTime-") String orderBy) {
        return ok(userService.findWithPaging(pageNum, pageSize, StrUtils.parseOrderBy(orderBy)));
    }

    @ApiOperation(value = "用户注册", notes = "新增用户") //TODO 放开权限
    @PostMapping(value = "/reg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Resp<?> doSignUp(@ApiParam(value = "注册资料", required = true) @Valid @RequestBody UserRegInfo regInfo) {
        User entity = userConverter.toEntity(regInfo);
        return userService.add(entity) ? ok() : fail();
    }

    @ApiOperation(value = "用户信息变更", notes = "修改用户信息")
    @PostMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<?> doChange(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody BasicUserInfo basicInfo) {
        User entity = userConverter.toEntity(basicInfo);
        return userService.modify(entity) ? ok() : fail();
    }

    @ApiOperation(value = "用户删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<?> doRemove(@RequestParam String[] uid) {
        String[] userIds = Arrays.stream(uid)
                .filter(StringUtils::isNoneBlank)
                .map(StringUtils::trim)
                .distinct()
                .toArray(String[]::new);

        if (userIds.length <= 0) {
            return fail("Invalid id list!");
        }

        userService.deleteByUsernames(uid);
        return ok();
    }

    @ApiOperation(value = "用户详情", notes = "根据手机号码来获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{mobile}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resp<BasicUserInfo> queryUserDetails(@PathVariable String mobile) {
        return ok(userConverter.toDto(userService.findByMobile(mobile)));
    }
}