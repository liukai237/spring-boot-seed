package com.iakuil.bf.web.security;

import com.google.common.collect.Sets;
import com.iakuil.bf.common.security.UserDetails;
import com.iakuil.bf.common.security.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * 数据库Realm
 *
 * @author Kai
 */
@Slf4j
public class JdbcRealm extends AuthorizingRealm {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        UserDetails details = userDetailsService.loadUserByUsername(username);
        if (details == null) {
            throw new UnknownAccountException("用户未注册");
        }

        String passwdHash = details.getPasswdHash();
        details.setPasswdHash(null);
        // 缓存不包含密码的用户详情对象
        return new SimpleAuthenticationInfo(
                details,
                passwdHash,
                getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserDetails userDetails = (UserDetails) principals.getPrimaryPrincipal();
        if (userDetails != null) {
            info.setRoles(userDetails.getRoles());

            // 如果是admin，通过PermissionUtils授予所有权限
            if ("admin".equals(userDetails.getUsername())) {
                info.setStringPermissions(Sets.newHashSet(PermissionUtils.getAllPerms()));
            } else {
                info.setStringPermissions(userDetails.getPermissions());
            }
        }

        return info;
    }
}
