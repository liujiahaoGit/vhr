package com.cicro.vhr.controller;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.service.UserServiceImpl;
import com.cicro.vhr.util.FastDFSUtils;
import com.cicro.vhr.util.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@RestController
public class LoginController {

    @Value("${fastDFS.host}")
    private String host;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/login")
    public Result login() {
        Result result = new Result(500, "尚未登录,请登录", false);
        return result;
    }

    //获取登录验证码
    @GetMapping("/code")
    public void verCode(HttpSession session, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        session.setAttribute("code", text);
        VerificationCode.output(image, response.getOutputStream());
    }

    @GetMapping("/hrInfo")
    public Result hrInfo(Authentication auth) {
        Hr hr = (Hr) auth.getPrincipal();
        return Result.SUCCESS(hr);
    }

    @PutMapping("/hrInfo")
    public Result updateHrInfo(@RequestBody Hr hr, Authentication auth) {
        Integer result = userService.updateHrById(hr);
        //在更新完数据库用户信息后 也要同时跟新security中的用户信息
        if (result == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr,
                auth.getCredentials(), auth.getAuthorities()));
            return Result.SUCCESS("更新成功");
        }
        return Result.FAIL("更新失败");
    }

    @PutMapping("/hrPass")
    public Result updateHrPass(@RequestBody Map<String, String> map, Authentication auth) {
        String oldPass = map.get("oldPass");
        String pass = map.get("pass");
        int hrId = Integer.parseInt(map.get("hrId"));

        Integer result = userService.updateHrPass(oldPass, pass, hrId);

        if (result == 1) {
            return Result.SUCCESS("更新成功");
        }
        return Result.FAIL("更新失败");
    }


    @PostMapping("/userFace")
    public Result updateHrPass(MultipartFile file, Authentication auth) {
        String s = FastDFSUtils.uploadFile(file);
        Hr hr = (Hr) auth.getPrincipal();
        hr.setUserface(host+s);
        Integer result = userService.updateHrById(hr);
        if (result == 1) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr,
                auth.getCredentials(), auth.getAuthorities()));
            return Result.SUCCESS("更新成功");
        }
        return Result.FAIL("更新失败");
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/employee/basic")
    public String hello1() {
        return "/emp/basic";
    }

    @RequestMapping("/employee/advanced")
    public String hello2() {
        return "/emp/adv";
    }
}
