package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import form.LoginForm;
import model.ResultMap;
import model.user.User;

@RestController
public class PlatformController {
	
    /**
     * 跳转到后台登录界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:/login");
        return view;
    }
    
    /**
     * 跳转到后台登录界面
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/login");
        return view;
    }
	
    /**
     * 登录
     * @param form
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ModelAndView login(@Valid LoginForm form, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        if (result.hasErrors()) {
            view.setViewName("redirect:/login");
            return view;
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated() && subject.getPrincipal() != null) {
                subject.logout();
            }
            subject.login(new UsernamePasswordToken(form.getUsername(), form.getPassword()));
        } catch (Exception e) {
            view.setViewName("redirect:/login");
            return view;
        }
        view.setViewName("redirect:/dashboard");
        return view;
    }
    
    /**
     * 跳转到后台的首页
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(method = RequestMethod.GET, value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/dashboard");
        return view;
    }
    
    /**
     * 退出后台
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ModelAndView logout() {
        ModelAndView view = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        view.setViewName("redirect:/login");
        return view;
    }
}
