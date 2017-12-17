package controller;

import form.UserForm;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;
import sun.misc.BASE64Encoder;
import utils.ResponseCode;
import utils.ResultData;

import java.security.MessageDigest;

/**
 * Created by sunshine on 16/12/2017.
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(UserForm form) {
        ResultData result = new ResultData();
        User user = new User();
        user.setUsername(form.getUsername());
        user.setManagername(form.getName());
        //实现对密码的加密
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String EncryNewPass = base64Encoder.encode(md.digest(form.getPassword().getBytes("utf-8")));
            user.setPassword(EncryNewPass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ResultData response = userService.create(user);
        result.setResponseCode(response.getResponseCode());
        try {
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(response.getData());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
