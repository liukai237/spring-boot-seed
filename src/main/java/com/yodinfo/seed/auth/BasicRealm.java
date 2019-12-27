package com.yodinfo.seed.auth;

import com.yodinfo.seed.service.UserService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BasicRealm extends AuthorizingRealm {

    protected UserService userService;

    public BasicRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Map<String, String> userDetail = (Map<String, String>) principalCollection.getPrimaryPrincipal();
        if (userDetail != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setRoles(split(MapUtils.getString(userDetail, "roles")));
            info.setStringPermissions(split(MapUtils.getString(userDetail, "perms")));
            return info;
        }

        return null;
    }

    private Set<String> split(String text) {
        return StringUtils.isBlank(text) ? null : Arrays.stream(text.split(",")).collect(Collectors.toSet());
    }
}