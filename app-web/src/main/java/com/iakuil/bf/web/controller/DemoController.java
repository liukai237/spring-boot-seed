package com.iakuil.bf.web.controller;

import com.iakuil.bf.common.BaseController;
import com.iakuil.bf.common.domain.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Slf4j
@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {

    @GetMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping(value = {"/", "/index"})
    public String toMain() {
        return SecurityUtils.getSubject().isAuthenticated() ? "index" : "redirect:/demo/login";
    }

    @GetMapping(value = "/welcome")
    public String toWelcome() {
        return "welcome";
    }

    @GetMapping(value = "/member-list")
    public String toMemberList() {
        return "member-list";
    }

    @GetMapping(value = "/statistics")
    public String toStatistics() {
        return "statistics";
    }

    @GetMapping(value = "/admin-role")
    public String toRoleList() {
        return "admin-role";
    }

    @GetMapping(value = "/role-add")
    public String toRoleAdd() {
        return "role-add";
    }

    @GetMapping(value = "/admin-rule")
    public String toRuleList() {
        return "admin-rule";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public Resp<?> signIn(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam(required = false, defaultValue = "off") String rememberMe) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, "on".equals(rememberMe)));
        return ok("登录成功！");
    }
}