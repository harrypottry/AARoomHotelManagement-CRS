package com.aaroom.utils;

/**
 * Created by sosoda on 2018/6/6.
 */

import com.aaroom.beans.Permission;
import com.aaroom.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = "/crssystem/*", filterName = "htmlFilter")
public class HtmlFilter extends HttpFilter {

    private final Logger log = LoggerFactory.getLogger(HtmlFilter.class);

    @Autowired
    private PermissionService permissionService;

    @Override
    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {


/*        //TODO：暂时对所有的链接全部通过
        filterChain.doFilter(request, response);
        return;*/

        //init
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        permissionService = webApplicationContext.getBean(PermissionService.class);

        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());

        if(!url.endsWith(".html")) {
            filterChain.doFilter(request, response);
            return;
        }

        //employee相关的网页访问权限
        if ("/crssystem/login.html".equals(url)) {
            filterChain.doFilter(request, response);
        } else {
            String redirectUrl = url.substring(0,url.indexOf("/", 2))+"/login.html";

            Integer user_id = (Integer) request.getSession().getAttribute("employee_id");
            if (user_id == null) {
                response.sendRedirect(redirectUrl);
            } else {
                List<Permission> permissions = permissionService.getPermissionUrlsByUserId(user_id,0);
                for (Permission permission: permissions) {
                    if(permission.getUrl().equals(url)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                if(permissions.size()>0) {
                    redirectUrl = permissions.get(0).getUrl();
                }
                response.sendRedirect(redirectUrl);
            }
        }

    }
}
