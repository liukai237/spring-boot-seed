package com.iakuil.bf.service;

import com.iakuil.bf.common.BaseService;
import com.iakuil.bf.dao.entity.MenuDO;
import com.iakuil.bf.dao.entity.RoleMenu;
import com.iakuil.bf.dao.RoleMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 菜单服务
 *
 * @author Kai
 */
@Slf4j
@Service
public class MenuService extends BaseService<MenuDO> {

    public final RoleMenuMapper roleMenuMapper;

    public MenuService(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Transactional(readOnly = true)
    public List<MenuDO> findByRoleId(Long roleId) {
        RoleMenu query = new RoleMenu();
        query.setRoleId(roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.select(query);
        return roleMenus.isEmpty() ? Collections.emptyList()
                : this.findByIds(roleMenus.stream().map(RoleMenu::getMenuId).toArray(Long[]::new));
    }
}
