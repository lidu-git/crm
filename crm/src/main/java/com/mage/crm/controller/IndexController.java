package com.mage.crm.controller;


import com.mage.crm.base.BaseController;
import com.mage.crm.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class IndexController extends BaseController {

    @RequestMapping("index")
    public String index(){
        return  "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest request) throws UnsupportedEncodingException {
       /* Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("userName")){
                request.setAttribute("userName",cookie.getValue());
            }
            if (cookie.getName().equals("trueName")){
                request.setAttribute("trueName", URLDecoder.decode(cookie.getValue(),"utf-8"));
            }
        }*/
        request.setAttribute("userName", CookieUtil.getCookieValue(request,"userName"));
        request.setAttribute("trueName",CookieUtil.getCookieValue(request,"trueName"));
        return "main";
    }
}
