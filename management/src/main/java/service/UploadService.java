package service;

import utils.ResultData;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by hushe on 2017/12/11.
 */
public interface UploadService {
    ResultData upload(MultipartFile file, String base);
}
