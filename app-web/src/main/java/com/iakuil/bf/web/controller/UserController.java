package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.PageData;
import com.iakuil.bf.common.PageRequest;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.service.converter.UserConverter;
import com.iakuil.bf.service.dto.UserDto;
import com.iakuil.bf.web.vo.UserEdit;
import com.iakuil.bf.web.vo.UserQuery;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户管理接口
 *
 * @author Kai
 */
@Slf4j
@Api(value = "UserController", tags = {"用户管理"})
@RestController
@RequestMapping("/api/user/")
public class UserController extends BaseController {
    private final UserService userService;

    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表，支持分页排序及条件查询。")
    @PostMapping(value = "/list")
    public Resp<PageData<UserDto>> doQueryWithPage(@Valid @RequestBody PageRequest<UserQuery> req) {
        return ok(userService.page(req.as(User.class), UserConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "用户信息变更", notes = "修改用户信息")
    @PostMapping(value = "/edit")
    public Resp<?> doChange(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody UserEdit param) {
        return ok(userService.modifyWithVersion(BeanUtils.copy(param, User.class)));
    }

    @ApiOperation(value = "用户删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/remove")
    public Resp<?> doRemove(@RequestParam Long id) {
        return ok(userService.removeById(id));
    }

    @ApiOperation(value = "用户批量注销", notes = "批量删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/batchRemove")
    public Resp<?> doRemoveBatch(@RequestParam Long[] ids) {
        return ok(userService.removeByIds(ids));
    }

    @ApiOperation(value = "用户详情", notes = "根据ID来获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{id}")
    public Resp<UserDto> queryUserDetails(@PathVariable Long id) {
        return ok(userConverter.toDto(userService.findById(id)));
    }
}