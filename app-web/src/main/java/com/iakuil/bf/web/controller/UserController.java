package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.PageData;
import com.iakuil.bf.common.PageRequest;
import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.db.Condition;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.bf.service.converter.UserConverter;
import com.iakuil.bf.service.dto.UserDetailDto;
import com.iakuil.bf.web.vo.UserAdd;
import com.iakuil.bf.web.vo.UserEdit;
import com.iakuil.bf.web.vo.UserQuery;
import com.iakuil.toolkit.BeanUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

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

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表，演示简单分页排序。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", defaultValue = "1", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", defaultValue = "10", dataType = "Long", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "sort", value = "排序规则", defaultValue = "createTime-", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "fields", value = "需求字段", dataType = "string", paramType = "query", example = "id,createTime"),
    })
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<PageData<User>> queryAllUsers(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false, defaultValue = "createTime-") String sort,
                                              @RequestParam String[] fields) {
        return ok(userService.page(Condition.Builder.init(User.class)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .orderByClause(sort)
                .include(fields)
                .build()));
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表，演示分页排序及条件查询。")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<PageData<UserDetailDto>> queryUsersWithPage(@Valid @RequestBody PageRequest<UserQuery> req) {
        return ok(userService.page(toQuery(req, User.class), UserConverter.INSTANCE::toDto));
    }

    @ApiOperation(value = "用户注册", notes = "新增用户")
    @PostMapping(value = "/reg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Resp<?> doSignUp(@ApiParam(value = "注册资料", required = true) @Valid @RequestBody UserAdd userInfo) {
        User user = new User();
        user.setTel(userInfo.getTel());
        user.setPasswdHash(userInfo.getPassword());
        return done(userService.add(user));
    }

    @ApiOperation(value = "用户信息变更", notes = "修改用户信息")
    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doChange(@ApiParam(value = "用户信息", required = true) @Valid @RequestBody UserEdit basicInfo) {
        return done(userService.modifyWithVersion(BeanUtils.copy(basicInfo, User.class)));
    }

    @ApiOperation(value = "用户删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doRemove(@RequestParam Long id) {
        userService.removeById(id);
        return ok();
    }

    @ApiOperation(value = "用户批量删除", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "用户ID，多个以逗号分隔", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/batchRemove", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resp<?> doBatchRemove(@RequestParam Long[] ids) {
        String[] userIds = Arrays.stream(ids)
                .distinct()
                .toArray(String[]::new);

        if (userIds.length <= 0) {
            return fail("Invalid id list!");
        }

        userService.removeByIds(ids);
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