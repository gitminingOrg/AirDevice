package service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IDGenerator;
import org.springframework.web.multipart.MultipartFile;
import service.UploadService;
import utils.ResponseCode;
import utils.ResultData;
import utils.SystemTeller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by hushe on 2017/12/11.
 */
public class UploadServiceImpl implements UploadService {
    private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public ResultData upload(MultipartFile file, String base) {
        ResultData result = new ResultData();
        try {
            if (file == null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }
        String PATH = "/material/upload";
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(current);
        StringBuilder builder = new StringBuilder(base);
        builder.append(PATH);
        builder.append("/");
        builder.append(time);
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String key = IDGenerator.generate("TH");
        String name = key + suffix;
        String completeName = builder.append(File.separator).append(name).toString();
        File temp = new File(completeName);
        try {
            file.transferTo(temp);
            int index = temp.getPath().indexOf(SystemTeller.tellPath(PATH + "/" + time));
            result.setData(temp.getPath().substring(index));
        } catch (IOException e) {
            logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}

