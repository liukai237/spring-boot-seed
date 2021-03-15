package com.iakuil.bf.shiro;

import com.iakuil.bf.common.UserDetails;
import com.iakuil.bf.dao.entity.Power;
import com.iakuil.bf.dao.entity.Role;
import com.iakuil.bf.dao.entity.User;
import com.iakuil.bf.service.UserService;
import com.iakuil.toolkit.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 数据库Realm
 *
 * @author Kai
 */
@Slf4j
public class JdbcRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        User user = userService.findByIdentity(username);
        if (user == null) {
            throw new UnknownAccountException("用户未注册");
        }

        // 封装不包含密码的用户详情对象
        UserDetails details = BeanUtils.copy(user, UserDetails.class);
        Long id = user.getId();
        details.setRoles(userService.findRolesByUserId(id).stream().map(Role::getRoleName).collect(Collectors.toSet()));
        details.setPermissions(userService.findPowersByUserId(id).stream().map(Power::getPowerName).collect(Collectors.toSet()));
        return new SimpleAuthenticationInfo(
                details,
                user.getPasswdHash(),
                getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserDetails userDetails = (UserDetails) principals.getPrimaryPrincipal();
        if (userDetails != null) {
            info.setRoles(userDetails.getRoles());
            info.setStringPermissions(userDetails.getPermissions());
        }
        return info;
    }
}
