package com.cicro.vhr.filter;

import com.cicro.vhr.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/*
 * @className: VerCodeFilter
 * @description 判断验证码的过滤器
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/8/12 0012
 * @version 1.0.0
 **/
@Component
public class VerCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if ("POST".equals(request.getMethod()) && "/doLogin".equals(request.getServletPath())) {
            String code = request.getParameter("code");
            String verCode = request.getSession().getAttribute("code").toString();
            if ("".equals(code) || null == code || !code.toLowerCase().equals(verCode.toLowerCase())) {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(Result.FAIL("验证码输入错误,请重新输入")));
                writer.flush();
                writer.close();
                return;
            }else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
