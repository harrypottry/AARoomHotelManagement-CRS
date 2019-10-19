package com.aaroom.utils;

/**
 * Created by sosoda on 2018/6/6.
 */

import com.aaroom.beans.Permission;
import com.aaroom.exception.RestError;
import com.aaroom.exception.UnauthorizedException;
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

@WebFilter(urlPatterns = "/wx/console/api/*", filterName = "WXConsoleFilter")
public class WechatFilter extends HttpFilter {

    private final Logger log = LoggerFactory.getLogger(WechatFilter.class);

    @Autowired
    private PermissionService permissionService;

    @Override
    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        //TODO 把微信方面的权限与employee结合起来
        //init
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        permissionService = webApplicationContext.getBean(PermissionService.class);

        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());


        Integer user_id = (Integer) request.getSession().getAttribute("employee_id");
        if (user_id == null) {
         throw new UnauthorizedException(RestError.E_AUTH_NEEDED);
          //return RestError.E_AUTH_NEEDED;
        } else {
            List<Permission> permissions = permissionService.getPermissionUrlsByUserId(user_id, 1);
            for (Permission permission : permissions) {
                if (permission.getUrl().equals(url)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            throw new UnauthorizedException(RestError.E_AUTH_NEEDED);

        }

    }
}
