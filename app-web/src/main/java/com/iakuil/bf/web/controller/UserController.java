package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.Req;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.db.PageData;
import com.iakuil.bf.common.tool.BeanUtils;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.service.converter.UserConverter;
import com.iakuil.bf.service.dto.UserDetailDto;
import com.iakuil.bf.web.query.UserAddParam;
import com.iakuil.bf.web.query.UserEditParam;
import com.iakuil.bf.web.query.UserPageQueryParam;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;

@Api(value = "用户管理", tags = {"用户管理"})
@RestController
@RequestMapping("/api/user/")
public class UserController extends BaseController {

    private final UserConverter userConverter;

    @Resource
    private UserService userService;

    public UserController(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表，简单分页排序。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", defaultValue = "0", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序规则", defaultValue = "createTime-", dataType = "string", paramType = "query"),
    })
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<PageData<UserDetailDto>> queryUsers(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                    @RequestParam(required = false, defaultValue = "0") Integer pageSize,
                                                    @RequestParam(required = false, defaultValue = "createTime-") String sort) {
        return ok(userService.findWithPage(pageNum, pageSize, sort));
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表，复杂分页排序。")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<PageData<UserDetailDto>> queryUsersByCondition(@RequestBody Req<UserPageQueryParam> req) {
        return ok(userService.findByCondition(req.getQuery()));
    }

    @ApiOperation(value = "用户注册", notes = "新增用户")
    @PostMapping(value = "/reg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Resp<?> doSignUp(@ApiParam(value = "注册资料", required = true) @Valid @RequestBody UserAddParam userInfo) {
        return done(userService.add(BeanUtils.copy(userInfo, User.class)));
    }

    @ApiOperation(value = "用户信息变更", notes = "修改用户信息")
    @PostMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doChange(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody UserEditParam basicInfo) {
        return done(userService.modify(BeanUtils.copy(basicInfo, User.class)));
    }

    @ApiOperation(value = "用户删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doRemove(@RequestParam Long id) {
        userService.deleteById(id);
        return ok();
    }

    @ApiOperation(value = "用户批量删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/batchRemove", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doBatchRemove(@RequestParam String[] ids) {
        String[] userIds = Arrays.stream(ids)
                .filter(StringUtils::isNoneBlank)
                .map(StringUtils::trim)
                .distinct()
                .toArray(String[]::new);

        if (userIds.length <= 0) {
            return fail("Invalid id list!");
        }

        userService.deleteByIds(ids);
        return ok();
    }

    @ApiOperation(value = "用户详情", notes = "根据UID来获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<UserDetailDto> queryUserDetails(@PathVariable Long id) {
        return ok(userConverter.toDto(userService.findById(id)));
    }
}