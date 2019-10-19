package com.aaroom.persistence;

import com.aaroom.beans.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);


    List<Permission> getPermissionUrlsByUserId(@Param("user_id") Integer user_id,
                                               @Param("type") Integer type);

    List<Permission> getMenusByUserId(@Param("user_id") Integer user_id,
                                      @Param("parent_id") Integer parent_id);

}