package com.aaroom.service;

import com.aaroom.beans.Permission;
import com.aaroom.persistence.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sosoda on 2018/7/16.
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> getPermissionUrlsByUserId(Object user_id) {
        return  getPermissionUrlsByUserId((Integer) user_id);
    }

    public List<Permission> getPermissionUrlsByUserId(Integer user_id) {
        return permissionMapper.getPermissionUrlsByUserId(user_id, null);
    }

    //type的属性：html为0，api为1
    public List<Permission> getPermissionUrlsByUserId(Integer user_id, Integer type) {
        return permissionMapper.getPermissionUrlsByUserId(user_id, type);
    }

    public List<Permission> getMenusByUserId(Integer user_id, Integer parent_id) {
        return permissionMapper.getMenusByUserId(user_id, parent_id);
    }
}
