package controller;

import com.alibaba.fastjson.JSON;
import form.UserForm;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;
import utils.Encryption;
import utils.ResponseCode;
import utils.ResultData;
import vo.user.UserVo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by sunshine on 16/12/2017.
 */
@CrossOrigin
@RestController
@RequestMapping("/managers")
public class ManagerController {
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(@Valid UserForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("表单含有非法参数");
            logger.error(JSON.toJSONString(br.getAllErrors()));
            return result;
        }
        User user = new User(form.getUsername(), form.getName(), Encryption.md5(form.getPassword()));
        ResultData response = userService.create(user);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData getUser() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = userService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            List<UserVo> list = (List<UserVo>)response.getData();
            result.setData(list);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setDescription(response.getDescription());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }
}
