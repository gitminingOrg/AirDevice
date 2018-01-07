package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import form.OrderCommodityForm;
import form.OrderCommodityWrapper;
import form.OrderCreateForm;
import form.OrderMissionForm;
import model.machine.Insight;
import model.order.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.OrderService;
import service.QRCodeService;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.GuoMaiOrderVo;
import vo.order.OrderVo;
import vo.user.UserVo;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private QRCodeService qRCodeService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    /**
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ModelAndView upload(MultipartHttpServletRequest request) throws IOException {
        ModelAndView view = new ModelAndView();
        MultipartFile file = request.getFile("orderFile");
        if (file.isEmpty()) {
            logger.info("上传的订单文件为空");
            view.setViewName("redirect:/order/overview");
            return view;
        }
        InputStream stream = file.getInputStream();
        CsvReader reader = new CsvReader(stream, Charset.forName("gbk"));
        List<String[]> list = new ArrayList<>();
        // 读取表头
        reader.readHeaders();
        while (reader.readRecord()) {
            list.add(reader.getValues());
        }
        reader.close();
        List<TaobaoOrder> order = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            TaobaoOrder item = new TaobaoOrder(list.get(i));
            order.add(item);
        }
        ResultData response = orderService.upload(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            logger.info("上传订单记录成功");
        } else {
            logger.info("上传订单记录失败");
        }
        view.setViewName("redirect:/order/overview");
        return view;
    }
     **/

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public DataTablePage<OrderVo> list(DataTableParam param) {
        DataTablePage<OrderVo> result = new DataTablePage<>(param);
        if (StringUtils.isEmpty(param)) {
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        ResultData response = orderService.fetch(condition, param);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result = (DataTablePage<OrderVo>) response.getData();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list(@RequestParam String param) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        JSONObject params = JSON.parseObject(param);
        if(!StringUtils.isEmpty(params)) {
            if(!StringUtils.isEmpty(params.get("channel"))) {
                condition.put("orderChannel", params.getString("channel"));
            }
            if(!StringUtils.isEmpty(params.get("status"))) {
                condition.put("orderStatus", params.getString("status"));
            }
            if (!StringUtils.isEmpty(params.get("startDate"))) {
                condition.put("startTime", params.getString("startDate"));
            }
            if (!StringUtils.isEmpty(params.get("endDate"))) {
                condition.put("endTime", params.getString("endDate"));
            }
            if (!StringUtils.isEmpty(params.get("province"))) {
                condition.put("province", params.getString("province"));
            }
        }
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("服务器忙，请稍后再试！");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
    public ModelAndView detail(@PathVariable("orderId") String orderId) {
        ModelAndView view = new ModelAndView();
        if (StringUtils.isEmpty(orderId)) {
            view.setViewName("redirect:/order/overview");
            return view;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            view.setViewName("redirect:/order/overview");
            return view;
        }
        GuoMaiOrderVo vo = ((List<GuoMaiOrderVo>) response.getData()).get(0);
        LocalDateTime time = LocalDateTime.ofInstant(vo.getOrderTime().toInstant(), TimeZone.getDefault().toZoneId());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String orderTime = time.format(format);
        view.addObject("order", vo);
        view.addObject("orderTime", orderTime);
        view.setViewName("/backend/order/detail");
        return view;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/deliver")
    public ResultData deliver(@PathVariable("orderId") String orderId, String productSerial, String shipNo) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(productSerial)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单编号和产品编号不正确");
            return result;
        }
        GuoMaiOrder order = new GuoMaiOrder();
        order.setOrderId(orderId);
        order.setShipNo(shipNo);
        order.setOrderStatus(OrderStatus.SHIPPED);
        ResultData response = orderService.assign(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("发货失败");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView create() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/create");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(@Valid OrderCreateForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        GuoMaiOrder order = new
                GuoMaiOrder(form.getOrderNo(), form.getBuyerName(), form.getBuyerAccount(), form.getReceiverName(),
                            form.getReceiverPhone(), form.getOrderPrice(), form.getProvince(), form.getCity(),
                            form.getDistrict(), form.getReceiverAddress(), form.getOrderChannel(),
                            form.getOrderCoupon(), Timestamp.valueOf(form.getOrderTime()), form.getDescription());
        if (form.getOrderDiversion() != null) {
            order.setOrderDiversion(form.getOrderDiversion());
        }
        ResultData response = orderService.create(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "{orderId}/update")
    public ResultData upload(@PathVariable String orderId, @Valid OrderCreateForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        GuoMaiOrder order = new
                GuoMaiOrder(form.getOrderNo(), form.getBuyerName(), form.getBuyerAccount(), form.getReceiverName(),
                form.getReceiverPhone(), form.getOrderPrice(), form.getProvince(), form.getCity(),
                form.getDistrict(), form.getReceiverAddress(), form.getOrderChannel(),
                form.getOrderCoupon(), Timestamp.valueOf(form.getOrderTime()), form.getDescription());
        order.setOrderId(orderId);
        if (form.getOrderDiversion() != null) {
            order.setOrderDiversion(form.getOrderDiversion());
        }
//        if (form.getOrderStatus() != null) {
        order.setOrderStatus(OrderStatus.convertToOrderStatus(form.getOrderStatus()));
//        }
        ResultData response = orderService.assign(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/commodity/create", consumes = "application/json")
    public ResultData createCommodity(@RequestBody OrderCommodityWrapper commodityWrapper, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        List<OrderCommodity> commodityList = new LinkedList<>();
        for (OrderCommodityForm form : commodityWrapper.getCommodities()) {
            form.setOrderId(commodityWrapper.getOrderId());
            OrderCommodity commodity = new
                    OrderCommodity(form.getOrderId(), form.getCommodityType(), form.getCommodityName(),
                    form.getCommodityPrice(), form.getCommodityQuantity(), form.getCommodityQrcode());
            commodityList.add(commodity);
        }

        ResultData response = orderService.uploadCommodity(commodityList);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/commodity/update", consumes = "application/json")
    public ResultData updateCommodity(@RequestBody OrderCommodityWrapper commodityWrapper, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        List<OrderCommodity> commodityList = new LinkedList<>();
        for (OrderCommodityForm form : commodityWrapper.getCommodities()) {
            form.setOrderId(commodityWrapper.getOrderId());
            OrderCommodity commodity = new
                    OrderCommodity(form.getOrderId(), form.getCommodityType(), form.getCommodityName(),
                    form.getCommodityPrice(), form.getCommodityQuantity(), form.getCommodityQrcode());
            commodity.setCommodityId(form.getCommodityId());
            commodityList.add(commodity);
        }

        ResultData response = orderService.assignBatchCommodity(commodityList);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/commodity/delete")
    public ResultData deleteCommodity(@RequestParam String commodityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("commodityId", commodityId);
        ResultData response = orderService.removeCommodity(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}/mission")
    public ResultData mission(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchMission4Order(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前此订单暂无任何事件记录");
        } else {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/mission/create")
    public ResultData missionCreate(@Valid OrderMissionForm form, BindingResult br,
                                    @PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated() || subject.getPrincipal() == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请先登录再进行操作");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前订单无法进行该操作");
            logger.error(result.getDescription());
            return result;
        }
        GuoMaiOrder order = new GuoMaiOrder();
        order.setOrderId(orderId);
        order.setOrderStatus(OrderStatus.INSTALLING);
        orderService.assign(order);
        UserVo user = (UserVo) subject.getPrincipal();
        OrderMission mission = new OrderMission(orderId, form.getMissionTitle(), form.getMissionContent(),
                user.getUsername(), form.getMissionChannel(), form.getMissionInstallType(),
                Timestamp.valueOf(form.getMissionDate()));
        response = orderService.create(mission);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        String codeId = "";
        if (!StringUtils.isEmpty(form.getCodeId())) {
            codeId = form.getCodeId();
        }
        String eventId = ((OrderMission) response.getData()).getMissionId();
        if (!StringUtils.isEmpty(form.getFilePath())) {
            String[] filepathList = form.getFilePath().split(";");
            for (String path : filepathList) {
                Insight insight = new Insight();
                insight.setPath(path);
                insight.setEventId(eventId);
                insight.setCodeId(codeId);
                response = qRCodeService.createInsight(insight);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(response.getResponseCode());
                    return result;
                }
            }
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/complete")
    public ResultData complete(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前订单无法进行该操作");
            logger.error(result.getDescription());
            return result;
        }
        GuoMaiOrderVo vo = ((List<GuoMaiOrderVo>) response.getData()).get(0);
        GuoMaiOrder order = new GuoMaiOrder();
        order.setOrderId(orderId);
        order.setShipNo(vo.getShipNo());
        order.setOrderStatus(OrderStatus.SUCCEED);
        order.setOrderPrice(vo.getOrderPrice());
        orderService.assign(order);
        return result;
    }



    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/cancel")
    public ResultData cancel(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前订单无法进行该操作");
            logger.error(result.getDescription());
            return result;
        }
        GuoMaiOrderVo vo = ((List<GuoMaiOrderVo>) response.getData()).get(0);
        GuoMaiOrder order = new GuoMaiOrder();
        order.setOrderId(orderId);
//        order.setProductSerial(vo.getProductSerial());
        order.setShipNo(vo.getShipNo());
        order.setOrderStatus(OrderStatus.REFUNDED);
        orderService.assign(order);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/status/list")
    public ResultData status() {
        ResultData result = new ResultData();
        ResultData response = orderService.fetchStatus();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orderstatus/list")
    public ResultData OrderStatus() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData response = orderService.fetchOrderStatus(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未获取到订单信息，请稍后重试！");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orderchannel/list")
    public ResultData OrderChannel() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchOrderChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器异常，请稍后重试");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderchannel/create")
    public ResultData createOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderService.create(orderChannel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/BatchUpload")
    public ModelAndView BatchUpload(MultipartHttpServletRequest request) throws IOException {
        ModelAndView view = new ModelAndView();
        MultipartFile file = request.getFile("orderFile");
        if (file.isEmpty()){
            logger.info("上传的文件为空");
            view.setViewName("redirect:/order/overview");
            return view;
        }
        InputStream stream = file.getInputStream();
        CsvReader reader = new CsvReader(stream, Charset.forName("gbk"));
        List<String[]> list = new ArrayList<>();
        // 读取表头
        reader.readHeaders();
        while (reader.readRecord()) {
            list.add(reader.getValues());
        }
        reader.close();
        List<GuoMaiOrder> order = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            GuoMaiOrder item = new GuoMaiOrder(list.get(i));
            order.add(item);
        }
        ResultData response = orderService.uploadBatch(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            logger.info("上传订单记录成功");
        } else {
            logger.info("上传订单记录失败");
        }
        view.setViewName("redirect:/order/overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/missionChannel/list")
    public ResultData missionChannelList() {
        ResultData result = new ResultData();
        Map<String, Object> map = new HashMap<>();
        map.put("blockFlag", false);
        ResultData response = orderService.fetchMissionChannel(map);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/missionChannel/create")
    public ResultData create(MissionChannel missionChannel) {
        ResultData result = new ResultData();
        ResultData response = orderService.create(missionChannel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }
}
