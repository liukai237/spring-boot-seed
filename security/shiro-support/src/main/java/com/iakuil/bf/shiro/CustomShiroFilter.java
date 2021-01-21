package com.iakuil.bf.shiro;

import com.iakuil.bf.common.Resp;
import com.iakuil.bf.common.constant.RespCode;
import com.iakuil.toolkit.JsonUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class CustomShiroFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        StringBuffer url = ((ShiroHttpServletResponse) resp).getRequest().getRequestURL();
        out.write(JsonUtils.bean2Json(new Resp(RespCode.UNAUTHORIZED)));
        out.flush();
        out.close();
        return false;
    }
}