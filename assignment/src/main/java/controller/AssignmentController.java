package controller;


import com.alibaba.fastjson.JSONObject;
import model.ResultMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunshine on 2017/4/30.
 */
@RestController
@RequestMapping("/assign")
public class AssignmentController {

    @RequestMapping("/inform")
    public ResultMap inform() {
        ResultMap result = new ResultMap();

        return result;
    }

    @RequestMapping("/locate")
    public ResultMap locate(String uid) {
        ResultMap result = new ResultMap();
        if (StringUtils.isEmpty(uid)) {
            result.setStatus(ResultMap.STATUS_FAILURE);
            result.setInfo("Param required");
        }
        JSONObject info = new JSONObject();
        info.put("uid", uid);
        info.put("ip", "");
        return result;
    }
}
