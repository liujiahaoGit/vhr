package com.cicro.vhr.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
 * @className: MyUsernamePasswordFilter
 * @description 对使用JSON传参登录的支持(spring security默认是使用的key/vaule的方式登录)
 * @since JDK1.8
 * @author ljh
 * @createdAt  2020/8/24 0024
 * @version 1.0.0
 **/

public class MyUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse resp) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
        }
        String verifyCode = request.getSession().getAttribute("code").toString();
        String username = null;
        String password = null;

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {

            try {
                Map map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                username = map.get("username").toString();
                password = map.get("password").toString();
                String code = map.get("code").toString();
                checkCode(code,verifyCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            username = username.trim();

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        }else {
            checkCode(request.getParameter("code"),verifyCode);
            return super.attemptAuthentication(request, resp);
        }

    }

    private void checkCode(String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("验证码不正确");
        }
    }
}
