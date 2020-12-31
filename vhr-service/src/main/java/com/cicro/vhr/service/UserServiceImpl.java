package com.cicro.vhr.service;

import com.cicro.vhr.mapper.HrMapper;
import com.cicro.vhr.mapper.HrRoleMapper;
import com.cicro.vhr.mapper.RoleMapper;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.model.Role;
import com.cicro.vhr.util.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @className: UserServiceImpl
 * @description 登录服务
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/7/15 0015
 * @version 1.0.0
 **/
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private HrMapper hrMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private HrRoleMapper hrRoleMapper;

    /*
     * @metnodName loadUserByUsername
     * @description 根据用户名获取当前用户信息
     * @param username
     * 用户名 这里只传入用户名,框架会根据用户名查到用户信息后 然后对前台传入的密码进行加密后和数据库查出的用户密码进行比对,如果一致则登录成功
     * @return UserDetails 用户信息
     * @throws
     * @author
     * @createdAt
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username); // select * from hr where username=#{username}
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        /*SELECT
            r.id,
            r.`name`,
            r.nameZh
          ROM
            role r
            LEFT JOIN hr_role hrr ON r.id = hrr.rid
            LEFT JOIN hr ON hr.id = hrr.hrid
          HERE
      	    hr.id =#{id}
        * */
        //获取当前用户所具备的角色信息 并设置进hr中
        List<Role> roles = roleMapper.getRoleListByHrId(hr.getId());
        hr.setRoles(roles);
        return hr;
    }

    /*
     * @methodName: getAllRoleByHrId
     * @description 根据当前用户ID获取其他用户及角色
     * @param:
     * @return:
     * @createdAt 15:00 2020/7/29 0029
     * @version 1.0.0
     **/
    public List<Hr> getAllRoleByHrId(String searchWord) {
        List<Hr> list = hrMapper.getAllRoleByHrId(HrUtils.getCurrentUser().getId(), searchWord);
        return list;
    }

    public Integer updateHrById(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    public List<Role> getAllRolesByHrId(Integer id) {
        return roleMapper.getAllRoleByHrId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateHrRoleById(Integer hid, Integer[] rids) {
        hrRoleMapper.deleteByHrid(hid);
        if (rids == null || rids.length == 0) {
            return true;
        }
        Integer integer = hrRoleMapper.insertRids(hid, rids);

        if (integer == rids.length) {
            return true;
        }
        return false;
    }

    public Integer deleteHrById(Integer id) {
        return hrMapper.deleteByPrimaryKey(id);

    }


    public Integer updateHrPass(String oldPass, String pass, Integer hrId) {
        Hr hr = hrMapper.selectByPrimaryKey(hrId);
        String password = hr.getPassword();//加密后的旧密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPass, password)) {
            String encode = encoder.encode(pass);  //加密后的新密码
            return hrMapper.updatePass(hrId, encode);
        }
        return -1;
    }
}
