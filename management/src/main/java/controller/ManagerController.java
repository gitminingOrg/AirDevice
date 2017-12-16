package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import utils.ResultData;

/**
 * Created by sunshine on 16/12/2017.
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create() {
        ResultData result = new ResultData();

        return result;
    }
}
