package com.cicro.vhr.filter;

import com.cicro.vhr.model.Menu;
import com.cicro.vhr.model.Role;
import com.cicro.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/*
 * @className: BeforeFilter
 * @description 所有请求在进系统前过滤判断
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/7/21 0021
 * @version 1.0.0
 **/
@Component
public class BeforeFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /*
     * @methodName: getAttributes
     * @description 根据访问的接口路径判断当前路径都需要哪些角色才能访问
     * @param:
     * @return:
     * @createdAt 18:23 2020/7/21 0021
     * @version 1.0.0
     **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        String url = ((FilterInvocation) o).getRequestUrl(); //得到当前的访问接口路径
        /*
        SELECT
      	m.*,
      	r.`name` as rname,
      	r.nameZh
      FROM
      	menu m
      	LEFT JOIN menu_role mr ON m.id = mr.mid
      	LEFT JOIN role r ON r.id = mr.rid
      	ORDER BY m.id
       */
        List<Menu> menus = menuService.getAllRolesByMenus(); //查出所有资源路径所需要的角色
        for (Menu menu : menus) {
            String[] str = null;
            if (antPathMatcher.match(menu.getUrl(), url)) {
                //访问当前资源路径所需要的角色
                List<Role> roles = menu.getRoles();
                str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(str);
            }

        }

        //如果所有的资源都没有匹配到,则设立标记(登录后可以访问)
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
