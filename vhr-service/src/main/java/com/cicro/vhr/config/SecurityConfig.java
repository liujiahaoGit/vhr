package com.cicro.vhr.config;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.filter.BeforeFilter;
import com.cicro.vhr.filter.MyUsernamePasswordFilter;
import com.cicro.vhr.filter.VerCodeFilter;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * @className: SecurityConfig
 * @description Security登录配置类
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/7/15 0015
 * @version 1.0.0
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BeforeFilter beforeFilter;

    @Autowired
    private DecisionManager decisionManager;

    @Autowired
    private VerCodeFilter codeFilter;

    /*
     * @metnodName bCryptPasswordEncoder
     * @description 配置密码加密
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * @methodName: myUsernamePasswordFilter
     * @description 将自定义的登录的filter注册bean
     **/
    @Bean
    MyUsernamePasswordFilter myUsernamePasswordFilter() throws Exception {
        MyUsernamePasswordFilter usernamePasswordFilter = new MyUsernamePasswordFilter();
        usernamePasswordFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse resp,
                                                Authentication auth) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter writer = resp.getWriter();
                Result result = new Result(ResultCode.SUCCESS);
                Hr hr = (Hr) auth.getPrincipal();
                hr.setPassword(null);
                result.setData(hr);
                result.setMessage("登录成功");
                writer.write(new ObjectMapper().writeValueAsString(result));  //登录成功后返回json 前端根据返回的信息进行页面跳转
                writer.flush();
                writer.close();
            }
        });
        usernamePasswordFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse resp,
                                                AuthenticationException e) throws IOException,
                ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter writer = resp.getWriter();
                Result result = new Result(500, null, false);
                if (e instanceof BadCredentialsException) {
                    result.setMessage("用户名或密码错误,请重新输入");
                } else if (e instanceof LockedException) {
                    result.setMessage("账户被锁定,请联系管理员");
                } else if (e instanceof DisabledException) {
                    result.setMessage("账户被禁用,请联系管理员");
                } else if (e instanceof CredentialsExpiredException) {
                    result.setMessage("密码已过期,登录失败");
                } else if (e instanceof AccountExpiredException) {
                    result.setMessage("账户已过期,登录失败");
                }
                writer.write(new ObjectMapper().writeValueAsString(result));
                writer.flush();
                writer.close();
            }
        });
        usernamePasswordFilter.setAuthenticationManager(authenticationManager());
        usernamePasswordFilter.setFilterProcessesUrl("/doLogin");
        return usernamePasswordFilter;
    }

    /*
     * @metnodName configure
     * @description 配置基于数据库的认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/login").mvcMatchers("/code"); //对登录接口和验证码放行
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录逻辑由security框架自动完成,所以将验证码的过滤器加到校验用户名和密码过滤器前面即可
        // http.addFilterBefore(codeFilter, UsernamePasswordAuthenticationFilter.class);
        // http.addFilterAt(myUsernamePasswordFilter(),UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
            //.anyRequest().authenticated() //任何的请求都要认证
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                @Override
                public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                    o.setAccessDecisionManager(decisionManager);  //注册自定义拦截器和访问管理器
                    o.setSecurityMetadataSource(beforeFilter);
                    return o;
                }
            })
            .and()
            .formLogin()
            .loginProcessingUrl("/doLogin") //登录的接口url (post请求)
            .loginPage("/login") //跳转的登录页面(这里因为是前后端分离,所以不跳转页面,后端返回json 页面调转交给前端处理)
            .usernameParameter("username") //登录的用户名
            .passwordParameter("password") //登录密码
            //配置登录成功的回调处理器
           /* .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
                                                    Authentication auth) throws IOException, ServletException {

                }
            }).failureHandler(new AuthenticationFailureHandler() {
            //配置失败的回调处理器
            @Override
            public void onAuthenticationFailure(HttpServletRequest req,
                                                HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {

            }
        })*/
            .permitAll()
            .and()
            .logout() //注销接口(get请求)
            //注销成功的回调处理器
            .logoutSuccessHandler(new LogoutSuccessHandler() {
                @Override
                public void onLogoutSuccess(HttpServletRequest req,
                                            HttpServletResponse resp, Authentication auth) throws IOException,
                    ServletException {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = resp.getWriter();
                    Result result = new Result(ResultCode.SUCCESS);
                    result.setMessage("注销成功");
                    writer.write(new ObjectMapper().writeValueAsString(result));
                    writer.flush();
                    writer.close();
                }
            }).permitAll()
            .and()
            .csrf().disable()//禁用csrf
            //没有认证时,在这里处理结果,不进行重定向
            .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest req, HttpServletResponse resp,
                                 AuthenticationException authException) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                resp.setStatus(HttpStatus.UNAUTHORIZED.value()); //返回相应码为401
                PrintWriter writer = resp.getWriter();
                Result result = new Result(500, null, false);
                if (authException instanceof InsufficientAuthenticationException) {
                    result.setMessage("操作异常,请登录后访问");
                }
                writer.write(new ObjectMapper().writeValueAsString(result));
                writer.flush();
                writer.close();
            }
        });
        http.addFilterAt(myUsernamePasswordFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
