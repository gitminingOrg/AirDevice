package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csvreader.CsvReader;
import form.*;
import model.machine.Insight;
import model.order.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.*;
import service.OrderService;
import utils.OrderConstant;
import utils.ResponseCode;
import utils.ResultData;
import vo.order.*;
import vo.user.UserVo;

import javax.crypto.Mac;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@EnableTransactionManagement
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private QRCodeService qRCodeService;

    @Autowired
    private MachineMissionService machineMissionService;

    @Autowired
    private OrderDiversionService orderDiversionService;

    @Autowired
    private MachineItemService machineItemService;

    @RequestMapping(method = RequestMethod.GET, value = "/overview")
    public ModelAndView overview() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/overview");
        return view;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request, @RequestParam String orderChannel){
        ResultData result = new ResultData();
        MultipartFile file;
        try {
            file = request.getFile("orderFile");
            if (file.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            String[] tempArray = file.getOriginalFilename().split("\\.");
            if (!tempArray[tempArray.length - 1].contains("csv")) {
                logger.info("上传的订单文件格式错误");
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("上传的订单文件格式错误");
                return result;
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
            List<OrderItem> itemList = new LinkedList<>();
            Map<String, Object> condition = new HashMap<>();
            condition.put("channelId", orderChannel);
            ResultData rd = orderService.fetchOrderChannel(condition);
            OrderChannelVo vo = ((List<OrderChannelVo>) rd.getData()).get(0);
            if (vo.getChannelName().equals("淘宝店铺")) {
                for (int i = 0; i < list.size(); i++) {
                    GuoMaiOrder item = GuoMaiOrder.convertFromTaoBao(list.get(i));
                    item.setOrderChannel(orderChannel);
                    condition.put("orderNo", item.getOrderNo());
                    rd = orderService.fetch(condition);
                    if (rd.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                        order.add(item);
                    }
                }
            } else if (vo.getChannelName().equals("京东")) {
                for (int i = 0; i < list.size(); i++) {
                    GuoMaiOrder item = GuoMaiOrder.convertFromJD(list.get(i));
                    item.setOrderChannel(orderChannel);
                    condition.put("orderNo", item.getOrderNo());
                    rd = orderService.fetch(condition);
                    if (rd.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                        order.add(item);
                    }
                }
            }
            if (!order.isEmpty()) {
                ResultData response = orderService.upload(order);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    logger.info("订单已上传");
                    if (vo.getChannelName().equals("京东")) {
                        for (int i = 0; i < order.size(); i++) {
                            condition.put("orderNo", order.get(i).getOrderNo());
                            rd = orderService.fetch(condition);
                            if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                GuoMaiOrderVo GVo = ((List<GuoMaiOrderVo>) rd.getData()).get(0);
                                OrderItem orderItem = new OrderItem(GVo.getOrderId(), OrderConstant.defaultOrderCommodityId,
                                        Integer.valueOf(list.get(i)[4]));
                                itemList.add(orderItem);
                            }
                        }
                    } else if (vo.getChannelName().equals("淘宝店铺")) {
                        for (int i = 0; i < order.size(); i++) {
                            condition.put("orderNo", order.get(i).getOrderNo());
                            rd = orderService.fetch(condition);
                            if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                GuoMaiOrderVo GVo = ((List<GuoMaiOrderVo>) rd.getData()).get(0);
                                OrderItem orderItem = new OrderItem(GVo.getOrderId(), OrderConstant.defaultOrderCommodityId,
                                        Integer.valueOf(list.get(i)[24]));
                                itemList.add(orderItem);
                            }
                        }
                    }
                    if (!itemList.isEmpty()) {
                        response = orderService.uploadOrderItem(itemList);
                        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            logger.info("OrderItem上传成功");
                            logger.info("订单记录上传成功");
                        }
                    } else {
                        logger.info("OrderItem为空，订单上传失败");
                    }
                }
            } else {
                logger.info("订单已存在，无需重复上传");
            }
            stream.close();
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            logger.error(e.getMessage());
            result.setDescription("服务器忙，请稍后再试");
        }
        return result;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/groupBuyingUpload")
    public ResultData upload(MultipartHttpServletRequest request) throws Exception {
        ResultData result = new ResultData();
        try {
            MultipartFile file = request.getFile("groupBuyingFile");
            InputStream stream = file.getInputStream();
            String[] tempList = file.getOriginalFilename().split("\\.");
            String fileAppendix = tempList[tempList.length - 1];
            if (file.isEmpty()) {
                logger.info("上传文件为空!");
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
            List<String[]> list = new ArrayList<>();
            if (fileAppendix.contains("xls")) {
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                XSSFSheet xssfSheet = workbook.getSheetAt(0);
                if (xssfSheet == null) {
                    result.setResponseCode(ResponseCode.RESPONSE_NULL);
                    return result;
                }
                for (int i = 1; i < xssfSheet.getLastRowNum() + 1; i++) {
                    XSSFRow row = xssfSheet.getRow(i);
                    String[] rowContent = new String[11];
                    for (int j = 0; j < 11; j++ ) {
                        Cell c = row.getCell(j);
                        if (c != null && c.getCellType() != Cell.CELL_TYPE_BLANK) {
                            rowContent[j] = c.toString();
                        }
                    }
                    if (rowContent[0] != null) {
                        list.add(rowContent);
                    }
                }
            } else if (fileAppendix.contains("csv")) {
                CsvReader reader = new CsvReader(stream, Charset.forName("gbk"));
                reader.readHeaders();
                while (reader.readRecord()) {
                    list.add(reader.getValues());
                }
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setData("格式不支持，请上传excel或者csv");
                return result;
            }
            List<GuoMaiOrder> preInstallOrders = new ArrayList<>();
            for (String[] orderParam : list) {
                GuoMaiOrder guoMaiOrder = GuoMaiOrder.convertFromGroupBuyingOrder(orderParam);
                if (guoMaiOrder != null) {
                    preInstallOrders.add(guoMaiOrder);
                }
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", false);
            ResultData response = orderService.fetch(condition);
            List<GuoMaiOrderVo> orderInstalled = new ArrayList<>();
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                orderInstalled = (List<GuoMaiOrderVo>) response.getData();
            }
            Set<String> orderNoInstalled = orderInstalled.stream().map(e -> e.getOrderNo()).collect(Collectors.toSet());
            preInstallOrders = preInstallOrders.stream().filter(e -> !orderNoInstalled.contains(e.getOrderNo())).collect(Collectors.toList());
            preInstallOrders = preInstallOrders.stream().filter(e -> e.getBuyerName() != null).collect(Collectors.toList());

            if (preInstallOrders.size() == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                logger.error("上传订单内容重复");
                result.setDescription("上传订单内容重复");
                return result;
            }

            // 对于没有引流上记录的引流商， 插入相应的引流商
            ResultData diversionResponse = orderDiversionService.fetch(new HashMap<>());
            if (diversionResponse.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
                List<OrderDiversionVo> diversionVoList = new ArrayList<>();
                if (diversionResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    diversionVoList = (List<OrderDiversionVo>) diversionResponse.getData();
                }
                Map<String, String> diversionMap = diversionVoList.stream().
                        collect(Collectors.toMap(e -> e.getDiversionName(), e -> e.getDiversionId()));
                for (GuoMaiOrder order : preInstallOrders) {
                    if (diversionMap.containsKey(order.getOrderDiversion())) {
                        order.setOrderDiversion(diversionMap.get(order.getOrderDiversion()));
                    } else {
                        OrderDiversion orderDiversion = new OrderDiversion(order.getOrderDiversion());
                        diversionResponse = orderDiversionService.create(orderDiversion);
                        if (diversionResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
                            orderDiversion = (OrderDiversion) diversionResponse.getData();
                        }
                        order.setOrderDiversion(orderDiversion.getDiversionId());
                        diversionMap.put(orderDiversion.getDiversionName(), orderDiversion.getDiversionId());
                    }
                }
            }

            response = orderService.upload(preInstallOrders);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<OrderItem> orderItemList = new ArrayList<>();
                preInstallOrders = (List<GuoMaiOrder>) response.getData();
                for (GuoMaiOrder order : preInstallOrders) {
                    if (order.getCommodityList() != null) {
                        for (OrderItem orderItem : order.getCommodityList()) {
                            orderItem.setOrderId(order.getOrderId());
                        }
                        orderItemList.addAll(order.getCommodityList());
                    }
                }
                response = orderService.uploadOrderItem(orderItemList);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("服务器忙，请稍后再试");
            }
            stream.close();
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            logger.error(e.getMessage());
            result.setDescription("服务器忙，请稍后再试");
        }
        return result;
    }

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
    public ResultData list(@RequestParam(required = false) String param) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        JSONObject params = JSON.parseObject(param);
        if (!StringUtils.isEmpty(params)) {
            if (!StringUtils.isEmpty(params.get("channel"))) {
                condition.put("orderChannel", params.getString("channel"));
            }
            if (!StringUtils.isEmpty(params.get("diversion"))) {
                condition.put("orderDiversion", params.getString("diversion"));
            }
            if (!StringUtils.isEmpty(params.get("status"))) {
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime time = LocalDateTime.ofInstant(vo.getOrderTime().toInstant(), TimeZone.getDefault().toZoneId());
        if (vo.getReceiveDate() != null) {
            LocalDateTime receive_time = LocalDateTime.ofInstant(vo.getReceiveDate().toInstant(), TimeZone.getDefault().toZoneId());
            String receiveTime = receive_time.format(format);
            view.addObject("receiveTime", receiveTime);
        }

        String orderTime = time.format(format);

        view.addObject("order", vo);
        view.addObject("orderTime", orderTime);

        view.setViewName("/backend/order/detail");
        return view;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/deliver")
    public ResultData deliver(@PathVariable("orderId") String orderId, String shipNo,
                              @RequestParam(required = false) String shipDescription)
    {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(orderId)) {
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
            OrderMission orderMission = new OrderMission();
            orderMission.setOrderId(orderId);
            orderMission.setMissionTitle("发货完成");
            orderMission.setMissionContent(shipDescription);
            Subject subject = SecurityUtils.getSubject();
            UserVo userVo = (UserVo) subject.getPrincipal();
            orderMission.setMissionRecorder(userVo.getUsername());
            orderService.create(orderMission);
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

    @Transactional
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
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderNo", order.getOrderNo());
        condition.put("blockFlag", false);
        ResultData response = orderService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("订单号已存在");
            return result;
        }
        response = orderService.create(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
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

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/create", consumes = "application/json")
    public ResultData createCommodity(@RequestBody OrderItemWrapper commodityWrapper, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        List<OrderItem> commodityList = new LinkedList<>();
        for (OrderItemForm form : commodityWrapper.getCommodities()) {
            form.setOrderId(commodityWrapper.getOrderId());
            OrderItem commodity = new
                    OrderItem(form.getOrderId(), form.getCommodityId(), form.getCommodityQuantity());
            commodityList.add(commodity);
        }

        ResultData response = orderService.uploadOrderItem(commodityList);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/update", consumes = "application/json")
    public ResultData updateCommodity(@RequestBody OrderItemWrapper commodityWrapper, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(JSONObject.toJSONString(br.getAllErrors()));
            return result;
        }
        List<OrderItem> commodityList = new LinkedList<>();
        for (OrderItemForm form : commodityWrapper.getCommodities()) {
            form.setOrderId(commodityWrapper.getOrderId());
            OrderItem commodity = new
                    OrderItem(form.getOrderId(), form.getCommodityId(), form.getCommodityQuantity());
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

    @RequestMapping(method = RequestMethod.POST, value = "/orderItem/delete")
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
        GuoMaiOrderVo guoMaiOrderVo = ((List<GuoMaiOrderVo>) response.getData()).get(0);
        GuoMaiOrder order = new GuoMaiOrder();
        order.setOrderStatus(guoMaiOrderVo.getOrderStatus());
        order.setOrderId(orderId);
//        order.setOrderStatus(OrderStatus.INSTALLING);
        orderService.assign(order);
        UserVo user = (UserVo) subject.getPrincipal();
        OrderMission mission = new OrderMission(orderId, form.getMissionTitle(), form.getMissionContent(),
                user.getUsername());
        response = orderService.create(mission);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
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

    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/receive")
    public ResultData receive(@PathVariable("orderId") String orderId, @RequestParam String receiveDate) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", 0);
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
        order.setOrderStatus(OrderStatus.RECEIVED);
        order.setOrderPrice(vo.getOrderPrice());

        //create order mission
        OrderMission orderMission = new OrderMission();
        orderMission.setOrderId(orderId);
        orderMission.setMissionTitle("收货事件");
        orderMission.setMissionContent("已经成功收货，收货时间：" + receiveDate);
        Subject subject = SecurityUtils.getSubject();
        UserVo userVo = (UserVo) subject.getPrincipal();
        orderMission.setMissionRecorder(userVo.getUsername());
        orderService.create(orderMission);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-M-d");
        LocalDate date = LocalDate.parse(receiveDate, formatter);
        order.setReceiveDate(Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
        orderService.assign(order);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/payed")
    public ResultData pay(@PathVariable("orderId") String orderId) {
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
        order.setOrderStatus(OrderStatus.PAYED);
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
        order.setShipNo(vo.getShipNo());
        order.setOrderStatus(OrderStatus.REFUNDED);
        order.setOrderPrice(vo.getOrderPrice());

        ResultData machineItemResponse = machineItemService.fetch(condition);
        if (machineItemResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineItemVo> machineItemVos = (List<MachineItemVo>) machineItemResponse.getData();
            List<MachineItem> machineItems = new ArrayList<>();
            for (MachineItemVo machineItemVo : machineItemVos) {
                MachineItem machineItem = new MachineItem();
                machineItem.setMachineId(machineItemVo.getMachineId());
                machineItem.setMachineMissionStatus(MachineMissionStatus.INSTALL_CANCEL);
                machineItems.add(machineItem);
            }
            machineItemService.updateBatch(machineItems);
        }
        orderService.assign(order);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{orderId}/dispatchProvider")
    public ResultData dispatchProvider(@PathVariable("orderId") String orderId, @RequestParam String providerId){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = machineItemService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前订单无法进行该操作");
            logger.error(result.getDescription());
            return result;
        }
        List<MachineItemVo> machineItemVos = (List<MachineItemVo>) response.getData();
        List<MachineItem> machineItems = new ArrayList<>();
        for (MachineItemVo machineItemVo : machineItemVos) {
            MachineItem machineItem = new MachineItem();
            machineItem.setMachineId(machineItemVo.getMachineId());
            machineItem.setProviderId(providerId);
            machineItem.setMachineMissionStatus(machineItemVo.getMachineMissionStatus());
            machineItems.add(machineItem);
        }
        response = machineItemService.updateBatch(machineItems);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }
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

    @RequestMapping(method = RequestMethod.GET, value = "/orderChannel/list")
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

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/create")
    public ResultData createOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderService.create(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/update")
    public ResultData updateOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = orderService.modifyOrderChannel(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/orderChannel/delete/{channelId}")
    public ResultData deleteOrderChannel(@PathVariable String channelId) {

        ResultData result = orderService.deleteOrderChannel(channelId);
        logger.info("delete orderChannel using channelId: " + channelId);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/BatchUpload")
    public ModelAndView BatchUpload(MultipartHttpServletRequest request) throws IOException {
        ModelAndView view = new ModelAndView();
        MultipartFile file = request.getFile("orderFile");
        if (file.isEmpty()) {
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
        ResultData response = orderService.upload(order);
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
    public ResultData create(SetupProvider missionChannel) {
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

    @RequestMapping(method = RequestMethod.POST, value = "/missionChannel/update")
    public ResultData updateMissionChannel(SetupProvider missionChannel) {
        ResultData result = new ResultData();
        ResultData response = orderService.modifyMissionChannel(missionChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/missionChannel/delete/{channelId}")
    public ResultData deleteMissionChannel(@PathVariable String channelId) {

        ResultData result = orderService.deleteMissionChannel(channelId);
        logger.info("delete missionChannel using channelId: " + channelId);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machineMission/list/{machineItemId}")
    public ResultData machineMissionlist(@PathVariable String machineItemId) {
        ResultData result = new ResultData();

        Map<String, Object> missionCondition = new HashMap<>();
        missionCondition.put("blockFlag", false);
        missionCondition.put("machineItemId", machineItemId);
        ResultData response = machineMissionService.fetch(missionCondition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machinemission/create")
    public ResultData createMachineMission(@Valid MachineMissionForm form, BindingResult br) {
        ResultData result = new ResultData();
        if (br.hasErrors()) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("表单中含有非法数据");
            logger.error(JSON.toJSONString(br.getAllErrors()));
            return result;
        }

        Subject subject = SecurityUtils.getSubject();
        UserVo userVo = (UserVo) subject.getPrincipal();
        MachineMission machineMission =
                new MachineMission(form.getMachineId(), form.getMissionTitle(), form.getMissionContent(),
                        userVo.getUsername(), Timestamp.valueOf(form.getMissionDate()));
        ResultData response = machineMissionService.create(machineMission);
        result.setResponseCode(response.getResponseCode());
        // update machine status
        if (form.getMachineStatusCode() != null) {
            MachineItem machineItem = new MachineItem();
            machineItem.setMachineId(form.getMachineId());
            machineItem.setInstallType(form.getMachineInstallType());
            machineItem.setMachineCode(form.getMachineQrcode());
            machineItem.setProviderId(form.getMachineProvider());
            machineItem.setMachineMissionStatus(
                    MachineMissionStatus.convertToMissionStatus(Integer.parseInt(form.getMachineStatusCode())));
            machineItemService.update(machineItem);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK && form.getFilePathList() != null) {
            result.setData(response.getData());
            MachineMission mission = (MachineMission) response.getData();
            String machine = mission.getMachineItemId();
            String missionId = mission.getMissionId();
            JSONArray filepathList = JSON.parseArray(form.getFilePathList());
            for (Object filepath : filepathList) {
                Insight insight = new Insight();
                insight.setMachineId(machine);
                insight.setEventId(missionId);
                insight.setPath((String) filepath);
                qRCodeService.createInsight(insight);
            }
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/diversion/list")
    public ResultData OrderDiversionList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderDiversionService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diversion/create")
    public ResultData CreateOrderDiversion(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionService.create(orderDiversion);
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

    @RequestMapping(method = RequestMethod.POST, value = "/diversion/update")
    public ResultData UpdateOrderDiversion(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionService.modify(orderDiversion);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/diversion/delete/{diversionId}")
    public ResultData DeleteOrderDiversion(@PathVariable String diversionId) {
        ResultData result = orderDiversionService.delete(diversionId);
        logger.info("delete orderDiversion using diversionId: " + diversionId);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machine/item/{machineItem}")
    public ModelAndView machineItemView(@PathVariable String machineItem) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineItem);
        condition.put("blockFlag", false);
        ResultData response = machineItemService.fetch(condition);
        MachineItemVo machineItemVo = ((List<MachineItemVo>) response.getData()).get(0);
        view.addObject("machineItem", machineItemVo);
        view.setViewName("/backend/order/orderMachine");
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orderMachine/detail")
    public ResultData getMachineItem(@RequestParam String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        condition.put("blockFlag", false);
        ResultData response = machineItemService.fetch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machineOverview/list")
    public ModelAndView orderMachineView() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/order_machine_overview");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machineItem/list")
    public DataTablePage<MachineItemVo> machineItemList(DataTableParam param) {
        ResultData response = machineItemService.fetch(param);
        DataTablePage<MachineItemVo> records = new DataTablePage<>();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return records;
        } else {
            return (DataTablePage<MachineItemVo>) response.getData();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/machineItem/list")
    public ResultData getMachineItemList(@RequestParam(required = false) String param) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        ResultData response = machineItemService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/machineItem/update")
    public ResultData updateMachine(MachineForm machineForm) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("machineId", machineForm.getMachineId());
        condition.put("blockFlag", 0);
        ResultData response = machineItemService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            MachineItemVo machineItemVo = ((List<MachineItemVo>) response.getData()).get(0);
            MachineItem machineItem = new MachineItem();
            machineItem.setMachineId(machineItemVo.getMachineId());
            machineItem.setProviderId(machineForm.getMachineProvider());
            machineItem.setInstallType(machineForm.getMachineInstallType());
            machineItem.setMachineCode(machineForm.getMachineCode());
            machineItem.setMachineMissionStatus(machineItemVo.getMachineMissionStatus());
            machineItem.setOrderItemId(machineItemVo.getOrderItemId());
            response = machineItemService.update(machineItem);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                result.setResponseCode(response.getResponseCode());
                result.setDescription(response.getDescription());
            } else {
                result.setData(response.getData());
            }
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orderConfig/view")
    public ModelAndView orderConfigView() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/order_config");
        return view;
    }
}
