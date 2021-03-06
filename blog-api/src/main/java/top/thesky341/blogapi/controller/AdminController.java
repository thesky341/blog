package top.thesky341.blogapi.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.thesky341.blogapi.entity.Admin;
import top.thesky341.blogapi.service.AdminService;
import top.thesky341.blogapi.util.Common;
import top.thesky341.blogapi.util.JWTUtil;
import top.thesky341.blogapi.util.encrypt.MD5SaltEncryption;
import top.thesky341.blogapi.util.result.Result;
import top.thesky341.blogapi.util.result.ResultUtil;
import top.thesky341.blogapi.util.result.ResultCode;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 *@author thesky
 *@date 2020/12/5
 */
@RestController
public class AdminController {
    @Resource(name = "resultUtil")
    private ResultUtil resultUtil;
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;
    @Resource(name = "jwtUtil")
    private JWTUtil jwtUtil;
    @Autowired
    private Common common;

    @RequiresAuthentication
    @PostMapping("/hello")
    public Result hello(HttpServletRequest request) {
        System.out.println(request.getHeader("Token"));
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        return resultUtil.success();
    }

    @RequiresAuthentication
    @PostMapping("/register")
    public Result register(@Valid @RequestBody Admin admin) {
        String encryptedPasswd = MD5SaltEncryption.encrypt(admin.getPasswd(), admin.getName());
        admin.setPasswd(encryptedPasswd);
        adminService.addAdmin(admin);
        return resultUtil.success("name", admin.getName());
    }

    @PostMapping("/login")
    public Result login(@Valid @RequestBody Admin admin) {
        UsernamePasswordToken token = new UsernamePasswordToken(admin.getName(), admin.getPasswd());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        admin = adminService.getAdminByName(admin.getName());
        return resultUtil.success();
    }

    @PostMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return resultUtil.success();
    }

    @PostMapping("/checkloginstate")
    public Result checkLoginState() {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()) {
            String name = common.getCurrentUserName();
            return resultUtil.success("name", name);
        } else {
            return resultUtil.getResult(ResultCode.UnauthenticatedException);
        }
    }

    /**
     * 由于使用了前后端分离，
     * 因此需要堵塞 Shiro 原有的登录接口
     */
    @RequestMapping("/shiro/login")
    public Result shiroLogin() {
        return resultUtil.getResult(ResultCode.NeedLogin);
    }
}
