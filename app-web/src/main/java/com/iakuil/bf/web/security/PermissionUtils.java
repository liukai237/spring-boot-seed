package com.iakuil.bf.web.security;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.*;

public class PermissionUtils {

    public static List<String> getAllPerms() {
        Reflections reflections = new Reflections("com.iakuil.bf.web.controller", Collections.singletonList(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequiresPermissions.class);

        Set<String> permissions = new HashSet<>();
        for (Method method : methods) {
            RequiresPermissions anno = method.getAnnotation(RequiresPermissions.class);
            Collections.addAll(permissions, anno.value());
        }
        return new ArrayList<>(permissions);
    }
}
