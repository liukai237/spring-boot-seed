package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.PageData;
import com.iakuil.bf.common.domain.PageRequest;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.service.converter.UserConverter;
import com.iakuil.bf.service.dto.UserDto;
import com.iakuil.bf.web.vo.UserEdit;
import com.iakuil.bf.web.vo.UserQuery;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @ApiOperation(value = "用户列表", notes = "支持分页排序及复杂条件查询。")
    @RequiresPermissions("sys:user:list")
    @PostMapping(value = "/list")
    public Resp<PageData<UserDto>> doQuery(@Valid @RequestBody PageRequest<UserQuery> req) {
        return ok(userService.page(req.as(User.class), UserConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "信息变更", notes = "管理员修改用户信息。")
    @RequiresPermissions("sys:user:edit")
    @PostMapping(value = "/edit")
    public Resp<?> doChange(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody UserEdit param) {
        return ok(userService.modifyWithVersion(BeanUtils.copy(param, User.class)));
    }

    @ApiOperation(value = "重置密码", notes = "管理员重置用户密码。")
    @RequiresPermissions("sys:user:resetPwd")
    @PostMapping(value = "/resetPwd")
    public Resp<?> doResetPwd(@RequestParam Long userId) {
        //TODO
        return ok();
    }

    @ApiOperation(value = "用户删除", notes = "管理员删除用户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @RequiresPermissions("sys:user:remove")
    @PostMapping(value = "/remove")
    public Resp<?> doRemove(@RequestParam Long id) {
        return ok(userService.removeById(id));
    }

    @ApiOperation(value = "批量删除", notes = "管理员批量删除用户。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @RequiresPermissions("sys:user:remove")
    @PostMapping(value = "/batchRemove")
    public Resp<?> doRemoveBatch(@RequestParam Long[] ids) {
        return ok(userService.removeByIds(ids));
    }

    @ApiOperation(value = "用户详情", notes = "根据ID来获取用户详细信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    })
    @RequiresPermissions("sys:user:list")
    @GetMapping(value = "/{id}")
    public Resp<UserDto> queryDetails(@PathVariable Long id) {
        return ok(userConverter.toDto(userService.findById(id)));
    }
}