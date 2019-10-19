package com.aaroom.restcontroller;

import com.aaroom.beans.Employee;
import com.aaroom.beans.Permission;
import com.aaroom.exception.RestError;
import com.aaroom.service.EmployeeService;
import com.aaroom.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sosoda on 2018/10/27.
 */
@RestController
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PermissionService permissionService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, String account, String password,
                        @RequestParam(value = "code", required = false) String code) {
        Employee user = employeeService.auth(account, password);
        Map<String, Object> ret = new HashMap<>();
        if (user != null) {
                request.getSession().setAttribute("employee_id", user.getId());
                List<Permission> permissions = permissionService.getPermissionUrlsByUserId(user.getId(), 0);
                if (permissions.size() > 0) {
                    for (Permission permission: permissions) {
                        if(permission.getUrl()!=null && permission.getUrl().length()>0) {
                            ret.put("message", permission.getUrl());
                            break;
                        }
                    }
                }
                ret.put("role_id", user.getRole_id()+"");
        } else {
            return RestError.E_AUTH_FAILED;
        }
        return new RestError(ret);
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Object logout(HttpServletRequest request) {

        request.getSession().removeAttribute("employee_id");

        return RestError.E_OK;
    }


    @RequestMapping(value = "/console/api/menus", method = RequestMethod.GET)
    public Object menus(HttpServletRequest request) {
        Integer user_id = (Integer) request.getSession().getAttribute("employee_id");
        List<Permission> permissions = permissionService.getMenusByUserId(user_id, 0);
        //menu第二层
        for (int i = 0; i < permissions.size(); i++) {
            Permission permission = permissions.get(i);
            List<Permission> subMenu = permissionService.getMenusByUserId(user_id, permission.getId());
            if (subMenu != null && subMenu.size() > 0) {
                permission.setPermissions(subMenu);
                permissions.set(i, permission);
            }
        }
        return permissions;
    }
}
