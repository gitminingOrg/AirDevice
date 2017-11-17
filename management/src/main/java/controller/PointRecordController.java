package controller;

import model.pointrecord.PointRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.OrderService;
import service.PointRecordService;
import utils.ResponseCode;
import utils.ResultData;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/pointRecord")
public class PointRecordController {

    @Autowired
    private PointRecordService pointRecordService;

    @RequestMapping(method = RequestMethod.GET, value = "/pointValue")
    public ResultData pointValueList() {
        Map<String, Object> condition = new HashMap<>();
        ResultData response =  pointRecordService.fetchPointValue(condition);
        ResultData result = new ResultData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器错误，轻稍后再试");
            return result;
        }
        return result;
    }
}
