package com.cicro.vhr.service;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.mapper.RoleMapper;
import com.cicro.vhr.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Result queryRole() {

        List<Role> jobRoles = roleMapper.queryRoles();
        return Result.SUCCESS(jobRoles);

    }

    public List<Integer> queryMenusIdByRoleId(Integer rid) {

        return roleMapper.queryMenusIdByRoleId(rid);

    }

    public void addRole(Role role) {
        String name = role.getName();
        if (name.startsWith("ROLE_")) {
            roleMapper.insert(role);
        } else {
            role.setName("ROLE_" + name);
            roleMapper.insert(role);
        }
    }

    public Integer delRoleById(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

   /* public Result addRole(Role jobLevel) {
        jobLevel.setEnabled(true);
        jobLevel.setCreateDate(new Date());
        int insert = RoleService.insert(jobLevel);
        Result result = null;
        if (insert > 0) {
            result = new Result(ResultCode.SUCCESS);
            result.setMessage("添加成功");
            return result;
        }
        result = new Result(ResultCode.FAIL);
        return result;
    }

    public Result updateRole(Role jobLevel) {
        int i = RoleService.updateByPrimaryKeySelective(jobLevel);
        Result result = null;
        if (i > 0) {
            result = new Result(ResultCode.SUCCESS);
            result.setMessage("修改成功");
            return result;
        }
        result = new Result(ResultCode.FAIL);
        return result;
    }

    public Result delRoleByIds(Integer[] ids) {
        int i = RoleService.delRoleByIds(ids);
        Result result = null;
        if (i > 0) {
            result = new Result(ResultCode.SUCCESS);
            result.setMessage("删除成功");
            return result;
        }
        result = new Result(ResultCode.FAIL);
        return result;
    }*/
}
