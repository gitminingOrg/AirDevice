package service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import config.ManagementConfig;
import dao.InsightDao;
import dao.QRCodeDao;
import dao.QRCodePreBindDao;
import model.goods.AbstractGoods;
import model.goods.RealGoods;
import model.machine.Insight;
import model.qrcode.PreBindCodeUID;
import model.qrcode.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pagination.DataTableParam;
import pagination.MobilePageParam;
import service.QRCodeService;
import utils.PathUtil;
import utils.QRSerialGenerator;
import utils.ResponseCode;
import utils.ResultData;
import vo.qrcode.QRCodeStatusVO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    private final int DEFAULT_WIDTH = 361, DEFAULT_HEIGHT = 361;

    private final static String SAVE_PATH = "/material/qrcode/";

    private final static String TEMPLATE_BG_PATH = "/material/backend/image/qrcode_bgv3.png";

    private final static int FORE_GROUND = 0xFF141830, BACK_GROUND = 0xFF6E7480;

    private Object lock = new Object();

    private Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);

    @Autowired
    private QRCodeDao qRCodeDao;

    @Autowired
    private QRCodePreBindDao qRCodePreBindDao;

    @Autowired
    private InsightDao insightDao;

    @Override
    public ResultData create(String goodsId, String modelId, String batchNo, int num) {
        ResultData result = new ResultData();
        if (num <= 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("需要批量生成的二维码的数量为:" + num + ", 无需进行处理");
            return result;
        }
        AbstractGoods goods = new RealGoods();
        goods.setGoodsId(goodsId);
        synchronized (lock) {
            DecimalFormat principle = new DecimalFormat("000");
            for (int i = 0; i < num; i++) {
                String value = new StringBuffer(batchNo).append(principle.format(i)).append(QRSerialGenerator.generate()).toString();
                String url = "http://" + ManagementConfig.getValue("domain_url") + ManagementConfig.getValue("qrcode_base") + "/" + value;
                // qrcode save folder
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String date = sdf.format(Calendar.getInstance().getTime());
                File file = new File(new StringBuffer(PathUtil.retrivePath()).append(SAVE_PATH).append(date).append(File.separator).toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                String path = new StringBuffer(SAVE_PATH).append(date).append(File.separator).append(value).append("_machine.png").toString();
                QRCode code = new QRCode(batchNo, value, path, url, goods, modelId);
                generate(code);
                combine(code.getPath(), value);
                qRCodeDao.insert(code);
            }
        }
        return result;
    }

    /**
     * 生成二维码图片
     *
     * @param code
     * @return
     */
    private ResultData generate(QRCode code) {
        ResultData result = new ResultData();
        // 保存图片
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            MatrixToImageConfig config = new MatrixToImageConfig(FORE_GROUND, BACK_GROUND);
            BitMatrix matrix = new MultiFormatWriter().encode(code.getUrl(), BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT, hints);
            File file = new File(new StringBuffer(PathUtil.retrivePath()).append(code.getPath()).toString());
            MatrixToImageWriter.writeToPath(matrix, QRCode.DEFAULT_FORMAT, file.toPath(), config);
            result.setData(code);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    private ResultData combine(String path, String value) {
        char[] item = value.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char i : item) {
            sb.append(i).append(" ");
        }
        value = sb.toString().trim();
        ResultData result = new ResultData();
        try {
            StringBuffer bg = new StringBuffer(PathUtil.retrivePath()).append(TEMPLATE_BG_PATH);
            StringBuffer qrcode = new StringBuffer(PathUtil.retrivePath()).append(path);
            BufferedImage big = ImageIO.read(new File(bg.toString()));
            BufferedImage small = ImageIO.read(new File(qrcode.toString()));
            Graphics2D g = big.createGraphics();
            int x = 437;
            int y = 461;
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            x = 200;
            y = 1080;
            Font font = new Font("arial", Font.PLAIN, 50);
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(value, x, y);
            y = 1270;
            g.drawString(value, x, y);
            g.dispose();
            ImageIO.write(big, "png", new File(qrcode.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData fetchByBatch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qRCodeDao.queryByBatch(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qRCodeDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition, DataTableParam param) {
        ResultData result = new ResultData();
        ResultData response = qRCodeDao.query(condition, param);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData deliver(QRCode code) {
        ResultData result = new ResultData();
        ResultData response = qRCodeDao.udpate(code);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            response.setDescription(response.getDescription());
        }
        return result;
    }

    private String format(String value) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            sb.append(value.charAt(i));
            if (i == 2 || i == 6 || i == 9) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    @Override
    public ResultData prebind(PreBindCodeUID pb) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("uId", pb.getUid());
        condition.put("codeId", pb.getCodeId());
        ResultData rs = qRCodePreBindDao.query(condition);
        if (rs.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            ResultData response = qRCodePreBindDao.insert(pb);
            result.setResponseCode(response.getResponseCode());
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(response.getData());
            } else {
                result.setDescription(response.getDescription());
            }
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该机器已预绑定，无需重复！");
            return result;
        }
    }

    @Override
    public ResultData fetchPreBind(Map<String, Object> condition) {
        return qRCodePreBindDao.query(condition);
    }

    @Override
    public ResultData fetchPreBind(Map<String, Object> condition, DataTableParam param) {
        return qRCodePreBindDao.query(condition, param);
    }

    @Override
    public ResultData fetchPreBind(Map<String, Object> condition, MobilePageParam param) {
        return qRCodePreBindDao.query(condition, param);
    }

    @Override
    public ResultData fetchPreBindByQrcode(String qrcode) {
        return qRCodePreBindDao.selectPreBindByQrcode(qrcode);
    }

    @Override
    public ResultData fetchPreBindByUid(String uid) {
        return qRCodePreBindDao.selectPreBindByUid(uid);
    }

    @Override
    public ResultData fetchDeviceChip(String uid) {
        return qRCodePreBindDao.selectChipDeviceByUid(uid);
    }

    @Override
    public ResultData fetchQrcodeStatus(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = qRCodeDao.queryQrcodeStatus(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<QRCodeStatusVO> list = (List<QRCodeStatusVO>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData fetchInsight(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = insightDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createInsight(Insight insight) {
        ResultData result = new ResultData();
        ResultData response = insightDao.insert(insight);
        result.setResponseCode(response.getResponseCode());
        if (result.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData createInsight(List<Insight> insights) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData deletePrebindByQrcode(String codeId) {
        ResultData result = qRCodePreBindDao.deletePreBindByQrcode(codeId);
        return result;
    }
}
