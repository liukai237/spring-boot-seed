package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import com.iakuil.bf.common.enums.Gender;
import com.iakuil.bf.common.enums.Province;
import com.iakuil.bf.common.tool.Dates;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(value = "DemoController", tags = {"示例接口"})
@Slf4j
@Controller
@RequestMapping("/demo/")
public class DemoController extends BaseController {

    private final UserService userService;

    public DemoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping(value = "/main")
    public String toMain() {
        return "main";
    }

    @ApiOperation(value = "登录接口", notes = "选择记住我后，关闭浏览器仍然可以继续访问主页。")
    @PostMapping(value = "/login")
    public String signIn(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam(required = false, defaultValue = "off") String rememberMe) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, "on".equals(rememberMe)));
        return "redirect:/demo/main";
    }

    @ApiOperation(value = "创建Mock用户", notes = "创建Mock用户，密码Changeme_123")
    @PostMapping(value = "/mock")
    @ResponseBody
    public Resp<?> createOneMockUser(@RequestParam String tel) {
        User user = new User();
        user.setNickname("手机用户" + tel);
        user.setPasswdHash("Changeme_123");
        user.setGender(Gender.FEMALE);
        user.setTel(tel);
        user.setProvince(Province.HN);
        user.setAddress("湖南省长沙市岳麓区麓谷科技园信息港B座1808XXOO科技有限公司");
        user.setAvatar("http://thirdwx.qlogo.cn/mmopen/xcTUUr83ib3bBt0py4IKHicd26icCIGDk3epiaLXDiaO51xcwzpZp7Cv5cK7ps4mqiaCDfHhZwPhRpXINoRUMowqpBcOZCR8y7Hn5u/132");
        user.setBirthday(Dates.parseDate("1999-02-13"));
        user.setEmail(tel + "@qq.com");
        userService.add(user);
        return ok();
    }
}