package com.humingfeng.controller;

import com.alibaba.fastjson.JSONObject;
import com.humingfeng.config.exception.CommonJsonException;
import com.humingfeng.service.LoginService;
import com.humingfeng.shiro.captcha.DreamCaptcha;
import com.humingfeng.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.humingfeng.constants.ErrorEnum.E_20012;

/**
*@ClassName     LoginController
*@description   登录相关Controller
*@author        胡铭锋
*@Date          2018/9/24 22:48
*@Version       1.0
**/
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private DreamCaptcha dreamCaptcha;

    @RequestMapping("/")
    public ModelAndView login() {

        ModelAndView mav = new ModelAndView("login/index");
        return mav;

    }

    @RequestMapping("/manage/index")
    public ModelAndView index() {

        ModelAndView mav = new ModelAndView();
        return mav;

    }

    /**
     * 登录
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/auth")
    public JSONObject authLogin(@RequestBody JSONObject requestJson,HttpServletRequest request, HttpServletResponse response) {

        //验证码
        String code = requestJson.getString("code");
        boolean validate = dreamCaptcha.validate(request, response, code);

        if(!validate){
            //验证码错误
            return CommonUtil.errorJson(E_20012);
        }


        CommonUtil.hasAllRequired(requestJson, "username,password,code");
        return loginService.authLogin(requestJson);
    }


    /**
     * 图形验证码 也可以使用Kaptcha实现
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        dreamCaptcha.generate(request, response);
    }


    /**
     * 查询当前登录用户的信息
     *
     * @return
     */
    @PostMapping("/getInfo")
    public JSONObject getInfo() {
        return loginService.getInfo();
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public JSONObject logout() {
        return loginService.logout();
    }
}
