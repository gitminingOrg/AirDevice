package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import form.QRCodeGenerateForm;
import model.qrcode.PreBindCodeUID;
import model.qrcode.QRCode;
import pagination.DataTablePage;
import pagination.DataTableParam;
import service.GoodsService;
import service.MachineService;
import service.QRCodeService;
import utils.IDGenerator;
import utils.PathUtil;
import utils.ResponseCode;
import utils.ResultData;
import utils.ZipUtil;
import vo.goods.GoodsModelVo;
import vo.machine.IdleMachineVo;
import vo.qrcode.QRCodeVo;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
	private Logger logger = LoggerFactory.getLogger(QRCodeController.class);

	@Autowired
	private QRCodeService qRCodeService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private MachineService machineService;

	@RequiresAuthentication
	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ModelAndView create() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/qrcode/create");
		return view;
	}

	@RequiresAuthentication
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/create")
	public ResultData create(QRCodeGenerateForm form) {
		ResultData result = new ResultData();
		String modelId = form.getModelId();
		Map<String, Object> condition = new HashMap<>();
		condition.put("modelId", modelId);
		if (StringUtils.isEmpty(modelId)) {
			logger.error("Empty Model_id Parameter: " + modelId);
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return result;
		}
		ResultData response = goodsService.fetchModel(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			logger.error("Incorrect Model_id Parameter: " + modelId);
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			return result;
		}
		GoodsModelVo vo = ((List<GoodsModelVo>) response.getData()).get(0);
		String batch = new StringBuffer(vo.getModelCode()).append(form.getBatchNo()).toString();
		response = qRCodeService.create(form.getGoodsId(), form.getModelId(), batch, form.getNum());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			String filename = generateZip(batch);
			result.setData(filename);
			return result;
		}
		result.setResponseCode(ResponseCode.RESPONSE_ERROR);
		result.setDescription("Sorry, the qrcodes are not generated as expected, please try agin.");
		return result;
	}

	private String generateZip(String batchNo) {
		// judge whether the batch no is illegal, including emptry and not exist
		if (StringUtils.isEmpty(batchNo)) {
			logger.info("The request with no specified batchNo cannot be executed");
			return "";
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("batchNo", batchNo);
		ResultData response = qRCodeService.fetchByBatch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			logger.info("The request with batchNo: " + batchNo + " cannot be executed");
			return "";
		}
		// read all qrcodes in the batch, generate a zip file
		String base = PathUtil.retrivePath();
		File directory = new File(new StringBuffer(base).append("/material/zip").toString());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String tempSerial = IDGenerator.generate("ZIP");
		File zip = new File(
				new StringBuffer(base).append("/material/zip/").append(tempSerial).append(".zip").toString());
		if (!zip.exists()) {
			try {
				zip.createNewFile();
			} catch (IOException e) {
				logger.error(e.getMessage());
				return "";
			}
		}
		response = qRCodeService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			logger.error(response.getDescription());
		}
		List<QRCodeVo> list = (List<QRCodeVo>) response.getData();
		File[] files = new File[list.size()];
		for (int i = 0; i < list.size(); i++) {
			File file = new File(new StringBuffer(PathUtil.retrivePath()).append(list.get(i).getPath()).toString());
			files[i] = file;
		}
		boolean status = ZipUtil.zip(zip, files);
		if (status == true) {
			logger.info("生成压缩文件成功");
		} else {
			logger.error("生成压缩文件失败");
		}
		return tempSerial;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/download/{filename}")
	public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
		File file = null;
		if (filename.startsWith("ZIP")) {
			filename = filename + ".zip";
			file = new File(PathUtil.retrivePath() + "/material/zip/" + filename);
		}
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			// 刷新缓冲，写出
			bos.flush();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/batch")
	public ModelAndView qrBatch() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/qrcode/batch");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/batch/available")
	public ResultData batch(String goodsId, String modelId) {
		ResultData result = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		if (!StringUtils.isEmpty(goodsId)) {
			condition.put("goodsId", goodsId);
		}
		if (!StringUtils.isEmpty(modelId)) {
			condition.put("modelId", modelId);
		}
		ResultData response = qRCodeService.fetchByBatch(condition);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setResponseCode(ResponseCode.RESPONSE_NULL);
			result.setDescription("No batch available");
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/overview")
	public ModelAndView overview() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/qrcode/overview");
		return view;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/list")
	public DataTablePage<QRCodeVo> list(DataTableParam param) {
		DataTablePage<QRCodeVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("delivered", false);
		ResultData response = qRCodeService.fetch(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<QRCodeVo>) response.getData();
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/free/all")
	public ResultData getFreeQrcode() {
		ResultData resultData = new ResultData();
		Map<String, Object> condition = new HashMap<>();
		condition.put("deliverd", false);
		condition.put("occupied", false);
		ResultData response = qRCodeService.fetch(condition);

		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			resultData.setData(response.getData());
			return resultData;
		} else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
			resultData.setDescription("服务器异常，请稍后再试");
			return resultData;
		}
		return resultData;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/delivered")
	public DataTablePage<QRCodeVo> delivered(DataTableParam param) {
		DataTablePage<QRCodeVo> result = new DataTablePage<>(param);
		if (StringUtils.isEmpty(param)) {
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("delivered", true);
		ResultData response = qRCodeService.fetch(condition, param);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result = (DataTablePage<QRCodeVo>) response.getData();
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deliver")
	public ResultData deliver(String codeId) {
		ResultData result = new ResultData();
		if (StringUtils.isEmpty(codeId)) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("code id is neccessary for deliver action");
			return result;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("codeId", codeId);
		ResultData response = qRCodeService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("code id: " + codeId + " is invalid");
			return result;
		}
		QRCode code = new QRCode();
		code.setCodeId(codeId);
		code.setDelivered(true);
		code.setDeliverAt(new Timestamp(System.currentTimeMillis()));
		response = qRCodeService.deliver(code);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription("code id: " + codeId + " deliver failed");
			return result;
		}
		result.setData(code);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deliver/range")
	public ModelAndView deliver(String batchNo, String fromValue, String endValue) {
		ModelAndView view = new ModelAndView();
		if (StringUtils.isEmpty(batchNo) || StringUtils.isEmpty(fromValue) || StringUtils.isEmpty(endValue)) {
			view.setViewName("redirect:/qrcode/overview");
			return view;
		}
		Map<String, Object> condition = new HashMap<>();
		condition.put("batchNo", batchNo.trim());
		ResultData response = qRCodeService.fetch(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
			view.setViewName("redirect:/qrcode/overview");
			return view;
		}
		List<QRCodeVo> list = (List<QRCodeVo>) response.getData();
		for (int i = 0; i < list.size(); i++) {
			QRCodeVo current = list.get(i);
			if (!current.getValue().equals(fromValue.trim())) {
				list.remove(i);
				i--;
			} else {
				break;
			}
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			QRCodeVo current = list.get(i);
			if (!current.getValue().equals(endValue.trim())) {
				list.remove(i);
			} else {
				break;
			}
		}
		for (QRCodeVo item : list) {
			QRCode code = new QRCode();
			code.setCodeId(item.getCodeId());
			code.setDelivered(true);
			code.setDeliverAt(new Timestamp(System.currentTimeMillis()));
			response = qRCodeService.deliver(code);
			if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
				logger.error("deliver qrcode error. code id: " + item.getCodeId());
			}
		}
		view.setViewName("redirect:/qrcode/overview");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/prebind")
	public ModelAndView prebind() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/backend/qrcode/prebind");
		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/prebind")
	public ResultData prebind(String uid, String codeId) {
		ResultData result = new ResultData();
		PreBindCodeUID pb = new PreBindCodeUID(uid, codeId);
		ResultData response = qRCodeService.prebind(pb);
		result.setResponseCode(response.getResponseCode());
		if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
			Map<String, Object> condition = new HashMap<>();
			condition.put("uid", uid);
			response = machineService.fetchIdleMachine(condition);
			if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
				IdleMachineVo vo = ((List<IdleMachineVo>) response.getData()).get(0);
				condition.clear();
				condition.put("im_id", vo.getImId());
				response = machineService.updateIdleMachine(condition);
			}
			result.setData(response.getData());

		} else if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
			result.setDescription(response.getDescription());
		}
		return result;
	}
}
